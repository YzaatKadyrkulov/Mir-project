package kg.mir.Mirproject.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.authDto.ResetPasswordRequest;
import kg.mir.Mirproject.entities.ForgotPasswordToken;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.exception.NotFoundException;
import kg.mir.Mirproject.repository.ForgotPasswordTokenRepo;
import kg.mir.Mirproject.repository.UserRepository;
import kg.mir.Mirproject.service.ForgotPasswordTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordTokenServiceImpl implements ForgotPasswordTokenService {

    private final UserRepository userRepo;
    private final ForgotPasswordTokenRepo forgotPasswordTokenRepo;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SimpleResponse sendResetEmail(String email) {
        User user = userRepo.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("user with email: " + email + " not found!"));

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plus(Duration.ofMinutes(30));

        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setToken(token);
        forgotPasswordToken.setExpiryDate(expiryDate);
        forgotPasswordToken.setUser(user);
        forgotPasswordTokenRepo.save(forgotPasswordToken);

        String resetPasswordUrl = "http://localhost:5173/changePassword";
        String resetLink = String.format("%s?token=%s", resetPasswordUrl, token);

        log.info("Sending reset link to email: {}", email);
        return sendLinkToEmail(email, resetLink);
    }

    @Override
    public SimpleResponse updateToNewPassword(ResetPasswordRequest resetPasswordRequest) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepo.getByToken(resetPasswordRequest.token())
                .orElseThrow(() -> new NotFoundException("Недействительный или просроченный токен"));

        if (!resetPasswordRequest.newPassword().equals(resetPasswordRequest.confirmNewPassword())) {
            log.warn("Password and confirmation password do not match");
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("Пароли не совпадают")
                    .build();
        }
        User user = forgotPasswordToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        userRepo.save(user);
        forgotPasswordTokenRepo.delete(forgotPasswordToken);

        log.info("Password successfully updated for user with email {}", user.getEmail());
        return new SimpleResponse(HttpStatus.OK, "Пароль успешно изменен");
    }

    private SimpleResponse sendLinkToEmail(String email, String resetLink) {
        String htmlMsg = """
                                 <!DOCTYPE html>
                                 <html lang="ru">
                                 <head>
                                     <meta charset="UTF-8">
                                     <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                     <title>Сброс пароля</title>
                                     <style>
                                         body {
                                             font-family: Arial, sans-serif;
                                             margin: 0;
                                             padding: 0;
                                             background-color: #f4f4f4;
                                         }
                                         .container {
                                             max-width: 600px;
                                             margin: 20px auto;
                                             background-color: #ffffff;
                                             border-radius: 8px;
                                             box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                                             position: relative;
                                             overflow: hidden;
                                         }
                                         .header, .footer-bar {
                                             background-color: #720dad;
                                             color: #ffffff;
                                             text-align: center;
                                             padding: 10px;
                                             font-size: 16px;
                                             font-weight: bold;
                                             width: 100%;
                                             box-sizing: border-box;
                                         }
                                         .header {
                                             border-radius: 8px 8px 0 0;
                                         }
                                         .footer-bar {
                                             border-radius: 0 0 8px 8px;
                                             position: absolute;
                                             bottom: 0;
                                             left: 0;
                                             box-sizing: border-box;
                                         }
                                         .content {
                                             text-align: center;
                                             padding: 20px;
                                         }
                                         .content p {
                                             font-size: 16px;
                                             color: #333333;
                                             margin-bottom: 10px;
                                         }
                                         .content a {
                                             display: inline-block;
                                             padding: 10px 20px;
                                             font-size: 16px;
                                             color: #ffffff;
                                             background-color: #720dad;
                                             text-decoration: none;
                                             border-radius: 5px;
                                             transition: background-color 0.3s ease;
                                         }
                                         .content a:hover {
                                             background-color: #5a0094;
                                         }
                                         .footer {
                                             text-align: center;
                                             padding: 10px;
                                             font-size: 12px;
                                             color: #777777;
                                             background-color: #ffffff;
                                             border-radius: 0 0 8px 8px;
                                             position: relative;
                                             z-index: 1;
                                         }
                                     </style>
                                 </head>
                                 <body>
                                     <div class="container">
                                         <div class="header">
                                             <h1>Сброс пароля</h1>
                                         </div>
                                         <div class="content">
                                             <p>Здравствуйте</p>
                                             <p>Для сброса пароля, нажмите на ссылку:</p>
                                             <a href=\"""" + resetLink + "\">Изменить пароль</a>" + """
                                         </div>
                                         <div class="footer">
                                             <p>Если вы не запрашивали изменение пароля, пожалуйста, проигнорируйте это сообщение.</p>
                                         </div>
                                         <div class="footer-bar">
                                             MIR
                                         </div>
                                     </div>
                                 </body>
                                 </html>
                                 """;
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setText(htmlMsg, true);
            helper.setTo(email);
            helper.setSubject("Забыли пароль!");
            helper.setFrom("user@gmail.com");
            javaMailSender.send(mimeMessage);

            log.info("Password reset link successfully sent to email {}", email);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Письмо успешно отправлено.")
                    .build();
        } catch (MessagingException e) {
            log.error("Failed to send email to {} due to {}", email, e.getMessage());
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Не удалось отправить письмо. Попробуйте еще раз позже.")
                    .build();
        }
    }
}