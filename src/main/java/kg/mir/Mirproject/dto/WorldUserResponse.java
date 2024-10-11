package kg.mir.Mirproject.dto;

import lombok.Builder;
@Builder
public record WorldUserResponse(
        String photoUrl,
        String userName,
        int userGoal,
        int userTotalSum
) {
}