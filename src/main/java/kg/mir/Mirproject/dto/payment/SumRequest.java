package kg.mir.Mirproject.dto.payment;

import jakarta.validation.constraints.NotNull;
import kg.mir.Mirproject.validation.EmailValidation;

public record SumRequest(
        @EmailValidation
        String email,
        @NotNull
        int sum
) {
}
