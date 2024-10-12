package kg.mir.Mirproject.repository;

import kg.mir.Mirproject.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment,Long> {
}
