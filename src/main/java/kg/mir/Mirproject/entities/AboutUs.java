package kg.mir.Mirproject.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "about_us")
public class AboutUs {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_id_gen")
    @SequenceGenerator(name = "base_id_gen", sequenceName = "about_us_seq", allocationSize = 1,initialValue = 10)
    private Long id;
    private String information;
}
