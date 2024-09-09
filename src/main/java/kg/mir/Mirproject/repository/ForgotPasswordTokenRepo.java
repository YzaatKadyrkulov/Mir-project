package kg.mir.Mirproject.repository;

import kg.mir.Mirproject.entities.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordTokenRepo extends JpaRepository<ForgotPasswordToken, Long> {
    Optional<ForgotPasswordToken> getByToken(String token);
}
