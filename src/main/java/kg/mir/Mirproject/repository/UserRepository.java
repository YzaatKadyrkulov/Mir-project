package kg.mir.Mirproject.repository;

import kg.mir.Mirproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User>getUserById(Long id);
}
