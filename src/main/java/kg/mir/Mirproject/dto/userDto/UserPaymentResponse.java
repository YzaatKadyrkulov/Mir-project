package kg.mir.Mirproject.dto.userDto;

import kg.mir.Mirproject.enums.Status;

import java.time.LocalDate;

public record UserPaymentResponse(
        LocalDate date,
        int sum,
        Status status) {
}
