package kg.mir.Mirproject.dto;

import kg.mir.Mirproject.dto.WorldDto.UserWorldResponse;
import lombok.*;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponse {
    private int globalSum;
    private List<UserWorldResponse> users;
}
