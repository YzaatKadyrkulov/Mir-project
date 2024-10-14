package kg.mir.Mirproject.dto.userDto;

import lombok.Builder;

@Builder
public record ProfileResponse(
        Long id,
        String photoUrl,
        String name,
        String number,
        int goal
) { }
