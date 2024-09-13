package kg.mir.Mirproject.dto.aboutUsDto.request;

import jakarta.validation.constraints.NotEmpty;

public record AboutUsRequest(

        @NotEmpty
        String information
) {
}
