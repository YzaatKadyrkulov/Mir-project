package kg.mir.Mirproject.entities;

import jakarta.persistence.Entity;
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
@SequenceGenerator(name = "base_id_gen", sequenceName = "about_us_seq", allocationSize = 1)
public class AboutUs extends BaseEntity {

    private String information;
}
