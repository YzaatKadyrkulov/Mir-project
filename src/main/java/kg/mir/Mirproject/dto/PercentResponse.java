package kg.mir.Mirproject.dto;

public record PercentResponse(
        Long userId,
        Integer sum,
        Double employees,
        Double insurance,
        Double program
) {
}