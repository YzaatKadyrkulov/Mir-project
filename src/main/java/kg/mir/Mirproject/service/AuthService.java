package kg.mir.Mirproject.service;

import jakarta.mail.MessagingException;
import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.authDto.AuthResponse;
import kg.mir.Mirproject.dto.authDto.SignInRequest;
import kg.mir.Mirproject.dto.authDto.SignUpRequest;
import kg.mir.Mirproject.dto.userDto.ResetPasswordRequest;

import java.io.IOException;

public interface AuthService {
    AuthResponse signUp(SignUpRequest signUpRequest);

    AuthResponse signIn(SignInRequest signInRequest);

    SimpleResponse forgotPassword(String email, String link);

    SimpleResponse resetPassword(ResetPasswordRequest request);
}
