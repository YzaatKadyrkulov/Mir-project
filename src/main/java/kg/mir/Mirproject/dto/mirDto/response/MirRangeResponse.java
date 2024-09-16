package kg.mir.Mirproject.dto.mirDto.response;

import lombok.Builder;

@Builder
public record MirRangeResponse(

        Integer min,

        Integer max
) {
}
