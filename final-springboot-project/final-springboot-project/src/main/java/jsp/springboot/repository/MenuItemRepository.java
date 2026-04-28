package jsp.springboot.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import jsp.springboot.entity.Item;

public interface MenuItemRepository extends JpaRepository<Item, Integer> {
	// 6. Get Items Greater than price
	List<Item> findByPriceGreaterThan(Double price);
	
	// 7. Get Item by name
	List<Item> findByItemName(String itemName);
}