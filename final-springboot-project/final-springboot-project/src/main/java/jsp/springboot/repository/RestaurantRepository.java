package jsp.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import jsp.springboot.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    
    // 6. Get Restaurant by Location
    List<Restaurant> findByLocation(String location);
    
    // 7. Get Restaurant by Name
    List<Restaurant> findByRestaurantName(String name);

}
