package kg.mir.Mirproject.dto;

import lombok.Builder;
@Builder
public record MirUsersResponse(
        Long id,
        String photoUrl,
        String userName,
        int userTotalSum,
        int userGoal
) {
}