package jsp.springboot.controller;

import org.springframework.data.domain.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Item;
import jsp.springboot.entity.Restaurant;
import jsp.springboot.service.RestaurantService;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService service;

    // 1. Add Restaurant 
    @PostMapping
    public ResponseEntity<ResponseStructure<Restaurant>> addRestaurant(@RequestBody Restaurant restaurant) {
        return service.addRestaurant(restaurant);
    }
    
    // 2. Get All Restaurants API
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Restaurant>>> getAllRestaurants() {
        return service.getAllRestaurants();
    }
    
    // 3. get Restaurant by id
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Restaurant>> getRestaurantById(@PathVariable Integer id) {
        return service.getRestaurantById(id);
    }
    
    // 4. update Restaurant
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<Restaurant>> updateRestaurant(@PathVariable Integer id, @RequestBody Restaurant restaurant) {
        return service.updateRestaurant(id, restaurant);
    }
    
    // 5. Delete Restaurant
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteRestaurant(@PathVariable Integer id) {
        return service.deleteRestaurant(id);
    }
    
    // 6. get Restaurant by location
    @GetMapping("/location/{location}")
    public ResponseEntity<ResponseStructure<List<Restaurant>>> getRestaurantByLocation(@PathVariable String location) {
        return service.getRestaurantByLocation(location);
    }
    
    // 7. Get Restaurant by Name
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseStructure<List<Restaurant>>> getRestaurantByName(@PathVariable String name) {
        return service.getRestaurantByName(name);
    }
    
    // 8. Get Menu Items of Restaurant
    @GetMapping("/{id}/items")
    public ResponseEntity<ResponseStructure<List<Item>>> getMenuItemsOfRestaurant(@PathVariable Integer id) {
        return service.getMenuItemsOfRestaurant(id);
    }
    
    // 9. Get Restaurants with Pagination and Sorting
    @GetMapping("/page/{pageNumber}/{pageSize}/{field}")
    public ResponseEntity<ResponseStructure<Page<Restaurant>>> getRestaurantsByPaginationAndSorting(@PathVariable int pageNumber, @PathVariable int pageSize, @PathVariable String field) {
        return service.getRestaurantsByPaginationAndSorting(pageNumber, pageSize, field);
    }
}
