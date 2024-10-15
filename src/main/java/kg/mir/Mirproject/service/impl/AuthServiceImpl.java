package kg.mir.Mirproject.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.authDto.AuthResponse;
import kg.mir.Mirproject.dto.authDto.SignInRequest;
import kg.mir.Mirproject.dto.authDto.SignUpRequest;
import kg.mir.Mirproject.dto.userDto.ResetPasswordRequest;
import kg.mir.Mirproject.entities.TotalSum;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.enums.Role;
import kg.mir.Mirproject.exception.AlreadyExistsException;
import kg.mir.Mirproject.exception.BadCredentialException;
import kg.mir.Mirproject.exception.NotFoundException;
import kg.mir.Mirproject.repository.TotalSumRepo;
import kg.mir.Mirproject.repository.UserRepository;
import kg.mir.Mirproject.config.security.jwt.JwtService;
import kg.mir.Mirproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static java.lang.String.format;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final TotalSumRepo totalSumRepo;

    @Override
    public AuthResponse signUp(SignUpRequest signUpRequest) {
        log.info("Попытка регистрации пользователя с email: {}", signUpRequest.email());

        if (userRepository.existsByEmail(signUpRequest.email())) {
            log.warn("Пользователь с email: {} уже существует", signUpRequest.email());
            throw new AlreadyExistsException("Пользователь с email: " + signUpRequest.email() + " уже существует");
        }

        String password = generateRandomPassword();
        User newUser = User.builder()
                .userName(signUpRequest.userName())
                .totalSum(signUpRequest.totalSum())
                .principalDebt(0)
                .goal(0)
                .paidDebt(0)
                .email(signUpRequest.email())
                .password(passwordEncoder.encode(password))
                .phoneNumber(signUpRequest.phoneNumber())
                .role(Role.USER)
                .build();
        userRepository.save(newUser);
        TotalSum totalSum = totalSumRepo.getTotalSumById(5L).orElseGet(() -> {
            TotalSum newTotalSum = new TotalSum();
            newTotalSum.setId(5L);
            newTotalSum.setTotalSum(0);
            return newTotalSum;
        });
        totalSum.setTotalSum(totalSum.getTotalSum() + newUser.getTotalSum());
        totalSumRepo.save(totalSum);
        log.info("Пользователь с email: {} успешно зарегистрирован", signUpRequest.email());

        Context context = new Context();
        context.setVariable("username", newUser.getUsername());
        context.setVariable("email", newUser.getEmail());
        context.setVariable("password", password);

        try {
            sendWelcomeEmail(context, newUser);
        } catch (Exception ex) {
            log.error("Ошибка при отправке письма подтверждения на адрес: {}", newUser.getEmail(), ex);
        }

        return AuthResponse.builder()
                .token(jwtService.generateToken(newUser))
                .email(newUser.getEmail())
                .role(newUser.getRole())
                .build();
    }

    private void sendWelcomeEmail(Context context, User newUser) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setSubject("Ассалому алейкум! Добро пожаловать в Мир");

        String message = String.format(
                "<!doctype html>" +
                        "<html lang=\"ru\">" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                        "<meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; line-height: 1.6; }" +
                        ".container { width: 80%%; margin: auto; background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        "h1 { color: #555; }" +
                        ".button { display: inline-block; background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; text-decoration: none; font-size: 16px; margin-top: 20px; border-radius: 5px; }" +
                        ".button:hover { background-color: #45a049; }" +
                        "p { margin-bottom: 20px; }" +
                        "</style>" +
                        "<title>Добро пожаловать в Мир</title>" +
                        "</head>" +
                        "<body>" +
                        "<div class=\"container\">" +
                        "<h1>Ассалому алейкум!</h1>" +
                        "<p>Уважаемый (-ая) <strong>%s</strong>,</p>" +
                        "<p>Спасибо за регистрацию! Ваш аккаунт был успешно создан.</p>" +
                        "<p>Ваш email: <strong>%s</strong></p>" +
                        "<p>Ваш пароль: <strong>%s</strong></p>" +
                        "<p>Мы рады приветствовать вас в нашем сообществе и ждем вас на нашем сайте.</p>" +
                        "<a href=\"https://yourwebsite.com\" class=\"button\">Посетить наш сайт</a>" +
                        "</div>" +
                        "</body>" +
                        "</html>",
                context.getVariable("username"),
                context.getVariable("email"),
                context.getVariable("password")
        );

        helper.setText(message, true);
        helper.setFrom("miravtoproject@gmail.com");
        helper.setTo(newUser.getEmail());
        javaMailSender.send(mimeMessage);
        log.info("Письмо с подтверждением отправлено на адрес: {}", newUser.getEmail());
    }

    @Override
    public AuthResponse signIn(SignInRequest signInRequest) {
        log.info("Попытка входа пользователя с email: {}", signInRequest.email());

        User user = userRepository.getUserByEmail(signInRequest.email())
                .orElseThrow(() -> {
                    log.error("Пользователь с email: {} не существует", signInRequest.email());
                    return new NotFoundException("Пользователь с email: " + signInRequest.email() + " не существует!");
                });

        if (signInRequest.email().isBlank()) {
            log.warn("Email пуст при попытке входа");
            throw new BadCredentialException("Email не может быть пустым!");
        }

        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            log.warn("Неверный пароль для пользователя с email: {}", signInRequest.email());
            throw new BadCredentialException("Неверный пароль!");
        }

        String token = jwtService.generateToken(user);
        log.info("Пользователь с email: {} успешно вошел", signInRequest.email());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @PostConstruct
    public void initSaveAdmin() {
        log.info("Попытка сохранить пользователя-администратора");
        User user = User.builder()
                .userName("Admin")
                .email("admin@gmail.com")
                .goal(0)
                .principalDebt(0)
                .totalSum(0)
                .password(passwordEncoder.encode("Admin123"))
                .role(Role.ADMIN)
                .build();
        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
            log.info("Пользователь-администратор с email: {} успешно сохранен", user.getEmail());
        } else {
            log.info("Пользователь-администратор с email: {} уже существует", user.getEmail());
        }
    }

    @Override
    public SimpleResponse forgotPassword(String email, String link) {
        log.info("Запрос на сброс пароля для email: {}", email);
        User user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException(format("Пользователь с email : %s не существует!", email)));
        String token = UUID.randomUUID().toString();
        user.setVerificationCode(token);
        userRepository.save(user);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("healthcheckjava@gmail.com");
            helper.setSubject("Сброс пароля");
            helper.setTo(email);
            Resource resource = new ClassPathResource("templates/forgot_password.html");
            String htmlContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String formattedHtmlContent = htmlContent.replace("${link}", link + "/" + token);
            helper.setText(formattedHtmlContent, true);
            javaMailSender.send(mimeMessage);
            log.info("Письмо для сброса пароля отправлено на адрес: {}", email);
        } catch (Exception ex) {
            log.error("Ошибка при отправке письма для сброса пароля на адрес: {}", email, ex);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Произошла ошибка при обработке запроса.")
                    .build();
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Проверьте электронную почту для получения инструкций по сбросу пароля.")
                .build();
    }

    @Override
    public SimpleResponse resetPassword(ResetPasswordRequest request) {
        log.info("Сброс пароля для токена: {}", request.token());
        try {
            User user = userRepository.findByResetToken(request.token())
                    .orElseThrow(() -> new NotFoundException("Неверный токен"));
            user.setPassword(passwordEncoder.encode(request.newPassword()));
            user.setVerificationCode(null);
            userRepository.save(user);
            log.info("Пароль пользователя с токеном: {} успешно сброшен", request.token());
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Пароль успешно сброшен.")
                    .build();
        } catch (Exception ex) {
            log.error("Ошибка при сбросе пароля для токена: {}", request.token(), ex);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Произошла ошибка при сбросе пароля.")
                    .build();
        }
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }
}