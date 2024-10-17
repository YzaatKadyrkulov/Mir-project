package kg.mir.Mirproject.dto.payment;

import lombok.Builder;

@Builder
public record DebtResponse(
        String debtSum
        ) {
}
