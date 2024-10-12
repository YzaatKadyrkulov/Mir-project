package kg.mir.Mirproject.dto.WorldDto;

import lombok.Builder;

@Builder
public record DebtResponse(
        String debtSum,
        int employees,
        int insurance,
        int program) {
}
