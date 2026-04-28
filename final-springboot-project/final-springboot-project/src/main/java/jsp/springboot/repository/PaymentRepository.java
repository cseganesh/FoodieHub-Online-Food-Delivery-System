package jsp.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import jsp.springboot.entity.Payment;
import jsp.springboot.enums.PaymentMethod;
import jsp.springboot.enums.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	// 2. Get payment by status
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
    
    // 3. Get payment by payment method
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
}
