package kg.mir.Mirproject.dto.aboutUsDto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AboutUsRequest(

        @NotNull
        Long id,

        @NotEmpty
        String information
) {
}
