package kg.mir.Mirproject.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.REFRESH;

@Entity
@Table(name = "worlds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "base_id_gen", sequenceName = "world_seq", allocationSize = 1)
public class World extends BaseEntity{
    private int rating;
    private Long minAmount;
    private Long maxAmount;

    @OneToMany(mappedBy = "world", cascade = {REFRESH})
    private List<User> users;
}
