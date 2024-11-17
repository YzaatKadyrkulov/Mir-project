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
    private Double employees;
    private Double insurance;
    private Double program;

    public AdminResponse(int globalSum, List<AllUsersResponse> users) {
        this.globalSum = globalSum;
        this.users = users;
    }

    public AdminResponse(int globalSum, Double employees, Double insurance, Double program) {
        this.globalSum = globalSum;
        this.employees = employees;
        this.insurance = insurance;
        this.program = program;
    }
}
