package kg.mir.Mirproject.dto.authDto;

import kg.mir.Mirproject.validation.EmailValidation;
import kg.mir.Mirproject.validation.PasswordValidation;

public record SignUpRequest(
        @EmailValidation
        String email,
        @PasswordValidation
        String password
) {
}
