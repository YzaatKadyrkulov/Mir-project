package kg.mir.Mirproject.dto.authDto;

import kg.mir.Mirproject.validation.PasswordValidation;
import lombok.Builder;

@Builder
public record ResetPasswordRequest(
        String token,
        @PasswordValidation
        String newPassword,
        String confirmNewPassword
) {
}
