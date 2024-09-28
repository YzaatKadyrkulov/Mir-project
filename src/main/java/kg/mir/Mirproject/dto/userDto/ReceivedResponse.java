package kg.mir.Mirproject.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivedResponse {
    private Long id;
    private String userName;
    private String photoUrl;
    private int principalDebt;
    private int totalSum;
    private int remainingAmount;
    private List<UserPaymentResponse> payment;

}
