package kg.mir.Mirproject.dto.aboutUsDto.response;

import jakarta.validation.constraints.NotEmpty;

public record AboutUsResponse(
        @NotEmpty
        Long id,
        @NotEmpty
        String information
) {
}
