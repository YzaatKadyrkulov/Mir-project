package kg.mir.Mirproject.dto.calculation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculationResponse {
    private int employeeFee;
    private int insuranceFee;
    private int programFee;
}
