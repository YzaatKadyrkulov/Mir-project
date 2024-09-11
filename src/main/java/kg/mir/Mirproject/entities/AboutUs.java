package kg.mir.Mirproject.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "about-us")
public class AboutUs {

    @Id
    private Long id;

    private String information;
}
