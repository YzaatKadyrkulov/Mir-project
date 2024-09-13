package kg.mir.Mirproject.dto;

import lombok.Builder;

@Builder
public record ConfirmRegistrationRequest(
        String verificationCode
) {
}