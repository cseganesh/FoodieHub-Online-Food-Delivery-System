package jsp.springboot.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Item;
import jsp.springboot.entity.Restaurant;
import jsp.springboot.exception.ResourceNotFoundException;
import jsp.springboot.repository.RestaurantRepository;

@Service
public class RestaurantService {
	@Autowired
	private RestaurantRepository repository;
	
	// 1. Add Restaurant
	public ResponseEntity<ResponseStructure<Restaurant>> addRestaurant(Restaurant restaurant) {

	    if(restaurant.getRestaurantName() == null || restaurant.getRestaurantName().isEmpty())
	        throw new ResourceNotFoundException("Restaurant name is required");

	    if(restaurant.getLocation() == null || restaurant.getLocation().isEmpty())
	        throw new ResourceNotFoundException("Restaurant location is required");

	    ResponseStructure<Restaurant> response = new ResponseStructure<>();

	    response.setStatusCode(HttpStatus.CREATED.value());
	    response.setMessage("Restaurant created successfully");
	    response.setData(repository.save(restaurant));

	    return new ResponseEntity<ResponseStructure<Restaurant>>(response, HttpStatus.CREATED);
	}
	
	// 2. Get All Restaurants API
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getAllRestaurants() {
		List<Restaurant> restaurants = repository.findAll();
		ResponseStructure<List<Restaurant>> response = new ResponseStructure<List<Restaurant>>();
		if(!restaurants.isEmpty()) {
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Restaurants fetched successfully");
		    response.setData(restaurants);
		    return new ResponseEntity<ResponseStructure<List<Restaurant>>>(response, HttpStatus.OK);
		}
		else {
			throw new ResourceNotFoundException("No restaurants found");
		}
	}
	
	// 3. get Restaurant by id
	public ResponseEntity<ResponseStructure<Restaurant>> getRestaurantById(Integer id) {
	    Optional<Restaurant> opt = repository.findById(id);
	    ResponseStructure<Restaurant> response = new ResponseStructure<>();
	    if (opt.isPresent()) {
	    response.setStatusCode(HttpStatus.OK.value());
	    response.setMessage("Restaurant fetched successfully");
	    response.setData(opt.get());
	    return new ResponseEntity<ResponseStructure<Restaurant>>(response, HttpStatus.OK);
	    } else {
	    	throw new ResourceNotFoundException("Restaurant not found with id: " + id);
	    }
	}
	
	// 4. Update Restaurant
	public ResponseEntity<ResponseStructure<Restaurant>> updateRestaurant(Integer id, Restaurant restaurant) {
	    Optional<Restaurant> opt = repository.findById(id);
	    ResponseStructure<Restaurant> response = new ResponseStructure<>();
	    if (opt.isPresent()) {
	        restaurant.setRestaurantId(id);
	        Restaurant updatedRestaurant = repository.save(restaurant);
	        response.setStatusCode(HttpStatus.OK.value());
	        response.setMessage("Restaurant updated successfully");
	        response.setData(updatedRestaurant);
	        return new ResponseEntity<ResponseStructure<Restaurant>>(response, HttpStatus.OK);
	    } else {
	        throw new ResourceNotFoundException("Restaurant not found with id: " + id);
	    }
	}
	
	// 5. Delete Restaurant
	public ResponseEntity<ResponseStructure<String>> deleteRestaurant(Integer id) {
		Optional<Restaurant> opt = repository.findById(id);
		ResponseStructure<String> response = new ResponseStructure<>();
		if(opt.isPresent()) {
			repository.deleteById(id);
			response.setStatusCode(HttpStatus.OK.value());
		    response.setMessage("Restaurant deleted successfully");
		    response.setData("Deleted");
		    return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.OK);
		} else {
			throw new ResourceNotFoundException("Restaurant not found with id: " + id);
		}
	}
	
	// 6. Get Restaurant by Location
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getRestaurantByLocation(String location) {
	    List<Restaurant> restaurants = repository.findByLocation(location);
	    if (restaurants.isEmpty()) {
	        throw new ResourceNotFoundException("No restaurants found in location: " + location);
	    }
	    ResponseStructure<List<Restaurant>> response = new ResponseStructure<>();
	    response.setStatusCode(HttpStatus.OK.value());
	    response.setMessage("Restaurants fetched successfully");
	    response.setData(restaurants);
	    return new ResponseEntity<ResponseStructure<List<Restaurant>>>(response, HttpStatus.OK);
	}
	
	// 7. Get Restaurant by Name
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getRestaurantByName(String name) {
	    List<Restaurant> restaurants = repository.findByRestaurantName(name);
	    if (restaurants.isEmpty()) {
	        throw new ResourceNotFoundException("No restaurants found with name: " + name);
	    }
	    ResponseStructure<List<Restaurant>> response = new ResponseStructure<>();
	    response.setStatusCode(HttpStatus.OK.value());
	    response.setMessage("Restaurants fetched successfully");
	    response.setData(restaurants);
	    return new ResponseEntity<ResponseStructure<List<Restaurant>>>(response, HttpStatus.OK);
	}
	
	// 8. Get Menu Items of Restaurant
	public ResponseEntity<ResponseStructure<List<Item>>> getMenuItemsOfRestaurant(Integer restaurantId) {
	    Optional<Restaurant> opt = repository.findById(restaurantId);
	    if (opt.isEmpty()) {
	        throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
	    }
	    Restaurant restaurant = opt.get();
	    List<Item> items = restaurant.getItems();

	    if (items == null || items.isEmpty()) {
	        throw new ResourceNotFoundException("No menu items found for restaurant id: " + restaurantId);
	    }

	    ResponseStructure<List<Item>> response = new ResponseStructure<>();
	    response.setStatusCode(HttpStatus.OK.value());
	    response.setMessage("Menu items fetched successfully");
	    response.setData(items);
	    return new ResponseEntity<ResponseStructure<List<Item>>>(response, HttpStatus.OK);
	}
	
	// 9. Get Restaurants with Pagination and Sorting
	public ResponseEntity<ResponseStructure<Page<Restaurant>>> 
	getRestaurantsByPaginationAndSorting(int pageNumber, int pageSize, String field) {
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(field));
	    Page<Restaurant> restaurants = repository.findAll(pageable);
	    if (restaurants.isEmpty()) {
	        throw new ResourceNotFoundException("No restaurants found");
	    }

	    ResponseStructure<Page<Restaurant>> response = new ResponseStructure<>();
	    response.setStatusCode(HttpStatus.OK.value());
	    response.setMessage("Restaurants fetched successfully");
	    response.setData(restaurants);

	    return new ResponseEntity<ResponseStructure<Page<Restaurant>>>(response, HttpStatus.OK);
	}
}
