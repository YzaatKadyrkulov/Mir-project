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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_id_gen")
    @SequenceGenerator(name = "base_id_gen", sequenceName = "total_sum_seq", allocationSize = 1, initialValue = 10)
    private Long id;
    private int totalSum;
    private int percent1;
    private int percent2;
    private int percent3;
}
