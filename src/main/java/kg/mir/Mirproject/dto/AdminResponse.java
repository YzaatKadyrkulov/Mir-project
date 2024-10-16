package kg.mir.Mirproject.dto;

import kg.mir.Mirproject.dto.WorldDto.AllUsersResponse;
import lombok.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponse {
    private int globalSum;
    private List<AllUsersResponse> users;
}
