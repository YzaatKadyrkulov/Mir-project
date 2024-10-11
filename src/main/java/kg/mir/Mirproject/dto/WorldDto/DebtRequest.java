package kg.mir.Mirproject.dto.WorldDto;

import jakarta.validation.constraints.NotNull;

public record DebtRequest(
        @NotNull
        int debtSum) {
}
