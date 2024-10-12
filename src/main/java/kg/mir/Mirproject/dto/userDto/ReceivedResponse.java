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
    private String principalDebt;
    private String payDebt;
    private String remainingAmount;
    private List<UserPaymentResponse> payment;

}
