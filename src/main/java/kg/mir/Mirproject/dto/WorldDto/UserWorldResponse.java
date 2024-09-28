package kg.mir.Mirproject.dto.WorldDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWorldResponse {
    private String userName;
    private String email;
    private String phoneNumber;
    private int goal;
}
