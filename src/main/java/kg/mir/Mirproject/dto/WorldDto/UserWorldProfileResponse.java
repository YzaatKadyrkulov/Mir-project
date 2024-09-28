package kg.mir.Mirproject.dto.WorldDto;

import kg.mir.Mirproject.enums.Status;

public record UserWorldProfileResponse(
        String userName,
        int goal,
        Status status,
        int sum
) {
}
