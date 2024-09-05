package kg.mir.Mirproject.entities;

import jakarta.persistence.Entity;
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
@SequenceGenerator(name = "base_id_gen", sequenceName = "total_sum_seq", allocationSize = 1)
public class TotalSum extends BaseEntity{
    private int totalSum;
    private int percent1;
    private int percent2;
    private int percent3;
}
