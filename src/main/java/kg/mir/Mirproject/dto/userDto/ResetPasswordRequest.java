package kg.mir.Mirproject.dto.userDto;

import kg.mir.Mirproject.validation.PasswordValidation;

public record ResetPasswordRequest(
        String token,
        @PasswordValidation
        String newPassword
) {
}
