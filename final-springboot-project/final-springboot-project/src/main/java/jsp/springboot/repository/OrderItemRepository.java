package jsp.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import jsp.springboot.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{
	// 4. Get all order items of an order
    List<OrderItem> findByOrderOrderId(Integer orderId);
}
