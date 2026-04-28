package jsp.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import jsp.springboot.entity.Order;
import jsp.springboot.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	// 4. Get Orders Of A Customer
	List<Order> findByCustomerCustomerId(Integer customerId);
	
	// 7. Get Orders By Status
	List<Order> findByStatus(OrderStatus status);
	
	// 8. Get Orders By Date
	List<Order> findByOrderDateTimeBetween(LocalDateTime start, LocalDateTime end);
	
	// 9. Get Orders Between Total Amount Range
	List<Order> findByTotalAmountBetween(Double min, Double max);
	
	// 10. Get Orders Of A Particular Restaurant
	List<Order> findByOrderItemsItemRestaurantRestaurantId(Integer restaurantId);
}