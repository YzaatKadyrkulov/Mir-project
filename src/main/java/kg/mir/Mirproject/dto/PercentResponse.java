package kg.mir.Mirproject.dto;

public record PercentResponse(
        Long userId,
        Integer principalDebt,
        Double employees,
        Double insurance,
        Double program
) {
}