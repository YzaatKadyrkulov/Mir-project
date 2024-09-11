package kg.mir.Mirproject.repository;

import kg.mir.Mirproject.entities.AboutUs;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AboutUsRepository extends MongoRepository<AboutUs, Long> {

    Optional<AboutUs> findTopByOrderByIdDesc();
}
