package kg.mir.Mirproject.service;

import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.authDto.ResetPasswordRequest;

public interface ForgotPasswordTokenService {
    SimpleResponse sendResetEmail(String email);
    SimpleResponse updateToNewPassword(ResetPasswordRequest resetPasswordRequest);
}
