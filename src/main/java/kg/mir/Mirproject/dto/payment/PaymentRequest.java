package kg.mir.Mirproject.dto.payment;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kg.mir.Mirproject.enums.Status;

public record PaymentRequest(
        Integer sum,
        @Enumerated(EnumType.STRING)
        Status status) {
}
