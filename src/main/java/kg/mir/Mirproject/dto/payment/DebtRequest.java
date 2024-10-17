package kg.mir.Mirproject.dto.payment;

import jakarta.validation.constraints.NotNull;

public record DebtRequest(
        @NotNull
        int debtSum) {
}
