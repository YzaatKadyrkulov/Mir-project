package kg.mir.Mirproject.dto;

import lombok.Builder;
@Builder
public record WorldUserResponse(
        Long id,
        String photoUrl,
        String userName,
        int userGoal,
        int userTotalSum
) {
}