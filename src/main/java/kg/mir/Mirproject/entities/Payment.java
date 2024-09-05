package kg.mir.Mirproject.entities;

import jakarta.persistence.*;
import kg.mir.Mirproject.enums.Status;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "total_sums")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "base_id_gen", sequenceName = "payment_seq", allocationSize = 1)
public class Payment extends BaseEntity {
    private LocalDate date;
    private Integer sum;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = {REFRESH, REMOVE, MERGE})
    private User user;
}
