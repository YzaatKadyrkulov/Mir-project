package kg.mir.Mirproject.dto.userDto;

import kg.mir.Mirproject.enums.UserStatus;

public record UserStatusResponse(
        Long id,
        String userName,
        int totalSum,
        String photoUrl,
        UserStatus userStatus
) {
}
