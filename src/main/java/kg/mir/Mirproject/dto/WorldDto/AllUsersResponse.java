package kg.mir.Mirproject.dto.WorldDto;

import lombok.Builder;
@Builder
public record AllUsersResponse(
        Long id,
        String photoUrl,
        String userName,
        String email,
        String number,
        int userTotalSum
) {
}