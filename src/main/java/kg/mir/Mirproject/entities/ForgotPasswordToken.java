package kg.mir.Mirproject.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "forgot_password_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordToken {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "forgot_password_gen")
    @SequenceGenerator(name = "forgot_password_gen", sequenceName = "forgot_password_seq", allocationSize = 1)
    private Long id;
    private String token;
    private LocalDateTime expiryDate;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private User user;
}
