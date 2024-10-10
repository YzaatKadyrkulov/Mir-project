package kg.mir.Mirproject.dto.payment;

public record SumRequest(
        String email,
        String phoneNumber,
        int sum
) {}
