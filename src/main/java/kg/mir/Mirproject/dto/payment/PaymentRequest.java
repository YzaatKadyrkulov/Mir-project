package kg.mir.Mirproject.dto.payment;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import kg.mir.Mirproject.enums.Status;

public record PaymentRequest(
        @NotNull
        int sum,
        @Enumerated(EnumType.STRING)
        @NotNull
        Status status) {
}
