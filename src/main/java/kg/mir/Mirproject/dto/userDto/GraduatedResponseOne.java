package kg.mir.Mirproject.dto.userDto;

import lombok.Builder;

@Builder
public record GraduatedResponseOne(
        Long id,
        String userName,
        int totalSum,
        String photoUrl
) {
}
