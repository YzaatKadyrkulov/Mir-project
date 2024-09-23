package kg.mir.Mirproject.dto.userDto;

import jakarta.validation.constraints.NotNull;
import kg.mir.Mirproject.validation.PasswordValidation;

public record ProfileUpdateRequest(
        @NotNull
        String photoUrl,
        @NotNull
        String name,
        @PasswordValidation
        String phoneNumber,
        @NotNull(message = "Цель не может быть пустой")
        int goal
) {
}
