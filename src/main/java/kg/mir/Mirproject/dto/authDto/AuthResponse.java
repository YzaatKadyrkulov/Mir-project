package kg.mir.Mirproject.dto.authDto;

import kg.mir.Mirproject.enums.Role;
import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String email,
        Role role
) {
}
