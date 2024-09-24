package kg.mir.Mirproject.dto.authDto;

import kg.mir.Mirproject.validation.EmailValidation;
import kg.mir.Mirproject.validation.PasswordValidation;

public record SignUpRequest(
        String userName,
        @EmailValidation
        String email,
        @PasswordValidation
        String phoneNumber
) {
}
