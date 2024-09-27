package kg.mir.Mirproject.dto.userDto;

public record ResetPasswordRequest(
        String newPassword,
        String verifyPassword
) {
}
