package kg.mir.Mirproject.repository;

import kg.mir.Mirproject.entities.AboutUs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AboutUsRepository extends JpaRepository<AboutUs, Long> {
    Optional<AboutUs> findTopByOrderByIdDesc();
}
