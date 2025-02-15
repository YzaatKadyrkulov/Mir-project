package kg.mir.Mirproject.repository;

import kg.mir.Mirproject.dto.AdminResponse;
import kg.mir.Mirproject.entities.TotalSum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TotalSumRepo extends JpaRepository<TotalSum,Long> {
    Optional<TotalSum> getTotalSumById(Long id);
    @Query("SELECT t FROM TotalSum t")
    List<TotalSum> getAllTotalSum();
}
