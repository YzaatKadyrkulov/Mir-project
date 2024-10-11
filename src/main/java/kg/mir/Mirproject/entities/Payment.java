package kg.mir.Mirproject.entities;

import jakarta.persistence.*;
import kg.mir.Mirproject.enums.Status;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    @SequenceGenerator(name = "payment_seq", sequenceName = "payment_seq", allocationSize = 1,initialValue = 10)
    private Long id;
    private LocalDate date;
    private Integer sum;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = {REFRESH,MERGE})
    private User user;
}
