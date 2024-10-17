package kg.mir.Mirproject.dto.WorldDto;

import kg.mir.Mirproject.enums.UserStatus;
import lombok.Builder;
@Builder
public record AllUsersResponse(
        Long id,
        String photoUrl,
        String userName,
        String email,
        UserStatus userStatus,
        String number,
        int totalSum
) {
}