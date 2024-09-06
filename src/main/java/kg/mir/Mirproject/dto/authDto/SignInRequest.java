package kg.mir.Mirproject.dto.authDto;

import jakarta.validation.constraints.Email;
import kg.mir.Mirproject.validation.PasswordValidation;
import lombok.Builder;

@Builder
public record SignInRequest(
        @Email(message = "Email is invalid")
        String email,
        @PasswordValidation
        String password
) {
}
