package kg.mir.Mirproject.dto;

import lombok.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponse {
    private int globalSum;
    private List<MirUsersResponse> users;
}
