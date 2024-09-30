package kg.mir.Mirproject.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "total_sums")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalSum {
    @Id
    private Long id;
    private int totalSum;
}
