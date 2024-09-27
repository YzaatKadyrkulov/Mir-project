package kg.mir.Mirproject.service.impl;

import jakarta.annotation.PostConstruct;
import kg.mir.Mirproject.dto.authDto.AuthResponse;
import kg.mir.Mirproject.dto.authDto.SignInRequest;
import kg.mir.Mirproject.dto.authDto.SignUpRequest;
import kg.mir.Mirproject.dto.userDto.ResetPasswordRequest;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.enums.Role;
import kg.mir.Mirproject.exception.AlreadyExistsException;
import kg.mir.Mirproject.exception.BadCredentialException;
import kg.mir.Mirproject.exception.NotFoundException;
import kg.mir.Mirproject.repository.UserRepository;
import kg.mir.Mirproject.config.security.jwt.JwtService;
import kg.mir.Mirproject.service.AuthService;
import kg.mir.Mirproject.service.EmailService;
import kg.mir.Mirproject.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Override
    public AuthResponse signUp(SignUpRequest signUpRequest) {
        log.info("Attempting to sign up user with email: {}", signUpRequest.email());

        if (userRepository.existsByEmail(signUpRequest.email())) {
            log.warn("User with email: {} already exists", signUpRequest.email());
            throw new AlreadyExistsException("User with email: " + signUpRequest.email() + " is already exist");
        }
        User newUser = User.builder()
                .userName(signUpRequest.userName())
                .email(signUpRequest.email())
                .phoneNumber(signUpRequest.phoneNumber())
                .role(Role.USER)
                .build();

        userRepository.save(newUser);
        log.info("User with email: {} successfully signed up", signUpRequest.email());

        String token = jwtService.generateToken(newUser);

        String resetPasswordToken = tokenService.generateResetPasswordToken(signUpRequest.email());

        emailService.sendResetPasswordEmail(signUpRequest.email(), resetPasswordToken);

        return AuthResponse.builder()
                .token(token)
                .email(newUser.getEmail())
                .role(newUser.getRole())
                .build();
    }

    @Override
    public AuthResponse signIn(SignInRequest signInRequest) {
        log.info("Attempting to sign in user with email: {}", signInRequest.email());

        User user = userRepository.getUserByEmail(signInRequest.email())
                .orElseThrow(() -> {
                    log.error("User with email: {} doesn't exist", signInRequest.email());
                    return new NotFoundException("User with email: " + signInRequest.email() + " doesn't exist!");
                });

        if (signInRequest.email().isBlank()) {
            log.warn("Email is blank for sign-in attempt");
            throw new BadCredentialException("Email is blank!");
        }

        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            log.warn("Incorrect password for user with email: {}", signInRequest.email());
            throw new BadCredentialException("Wrong password!");
        }

        String token = jwtService.generateToken(user);
        log.info("User with email: {} successfully signed in", signInRequest.email());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest resetPasswordRequest) {
        String email = tokenService.getEmailFromToken(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found"));

        if (!resetPasswordRequest.newPassword().equals(resetPasswordRequest.verifyPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        userRepository.save(user);

        log.info("Password successfully updated for user: {}", user.getEmail());
    }

    @PostConstruct
    public void initSaveAdmin() {
        log.info("Attempting to save admin user");
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
            log.info("Admin user with email: {} successfully saved", user.getEmail());
        } else {
            log.info("Admin user with email: {} already exists", user.getEmail());
        }
    }
}