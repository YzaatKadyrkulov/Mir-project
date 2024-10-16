package kg.mir.Mirproject.dto.userDto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfileResponse(
        Long id,
        String photoUrl,
        String name,
        String number,
        int goal,
        int totalSum,
        int principalDebt,
        int payDebt,
        int remainingAmount,
        List<UserPaymentResponse> payment
) { }
