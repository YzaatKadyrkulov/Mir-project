package kg.mir.Mirproject.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class ProfileUpdateRequest {
    private String photoUrl;
    private String fullName;
    private String phoneNumber;
    private int goal;
}
