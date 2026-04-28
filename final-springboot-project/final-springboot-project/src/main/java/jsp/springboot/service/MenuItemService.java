package jsp.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Item;
import jsp.springboot.entity.Restaurant;
import jsp.springboot.exception.InvalidInputException;
import jsp.springboot.exception.ResourceNotFoundException;
import jsp.springboot.repository.MenuItemRepository;
import jsp.springboot.repository.RestaurantRepository;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository itemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    // 1. Add Menu Item
    public ResponseEntity<ResponseStructure<Item>> addMenuItem(Item item) {

        if(item.getPrice() < 0)
            throw new InvalidInputException("Price cannot be negative");

        if(item.getRestaurant() == null)
            throw new InvalidInputException("Restaurant required");

        Restaurant restaurant = restaurantRepository
                .findById(item.getRestaurant().getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        item.setRestaurant(restaurant);

        ResponseStructure<Item> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Menu item added successfully");
        response.setData(itemRepository.save(item));

        return new ResponseEntity<ResponseStructure<Item>>(response, HttpStatus.CREATED);
    }
    
    // 2. Get All Menu Item
    public ResponseEntity<ResponseStructure<List<Item>>> getAllMenuItems() {
    	List<Item> items = itemRepository.findAll();
    	if(!items.isEmpty()) {
    		ResponseStructure<List<Item>> response = new ResponseStructure<>();
    	    response.setStatusCode(HttpStatus.OK.value());
    	    response.setMessage("Menu items fetched successfully");
    	    response.setData(items);
    	    return new ResponseEntity<ResponseStructure<List<Item>>>(response, HttpStatus.OK);
    	} else {
    		throw new ResourceNotFoundException("No menu items found");
    	}
    }
    
    // 3. Get Menu Item By ID
    public ResponseEntity<ResponseStructure<Item>> getMenuItemById(Integer id){
    	Optional<Item> opt = itemRepository.findById(id);
    	ResponseStructure<Item> response = new ResponseStructure<>();
    	if(opt.isPresent()) {
    		response.setStatusCode(HttpStatus.OK.value());
    	    response.setMessage("Menu item fetched successfully");
    	    response.setData(opt.get());
    	    return new ResponseEntity<ResponseStructure<Item>>(response, HttpStatus.OK);
    	} else {
    		throw new ResourceNotFoundException("Menu item not found");
    	}
    }
    
    // 4. Update Price
    public ResponseEntity<ResponseStructure<Item>> updatePrice(Integer id, Double price) {
        if(price <= 0)
            throw new InvalidInputException("Price cannot be negative or Zero");
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
        item.setPrice(price);
        ResponseStructure<Item> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Price updated successfully");
        response.setData(itemRepository.save(item));
        return new ResponseEntity<ResponseStructure<Item>>(response, HttpStatus.OK);
    }
    
    // 5. Delete Menu Item
    public ResponseEntity<ResponseStructure<String>> deleteMenuItem(Integer id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
        itemRepository.delete(item);
        ResponseStructure<String> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Menu item deleted successfully");
        response.setData("Deleted");
        return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.OK);
    }
    
    // 6. Get Items Greater Than Price
    public ResponseEntity<ResponseStructure<List<Item>>> getItemsGreaterThanPrice(Double price) {
    	List<Item> items = itemRepository.findByPriceGreaterThan(price);
    	ResponseStructure<List<Item>> response = new ResponseStructure<>();
    	if(!items.isEmpty()) {
    		response.setStatusCode(HttpStatus.OK.value());
    	    response.setMessage("Items fetched successfully");
    	    response.setData(items);
    	    return new ResponseEntity<ResponseStructure<List<Item>>>(response, HttpStatus.OK);
    	} else {
    		throw new ResourceNotFoundException("No items found greater than given price");
    	}
    }
    
    // 7. Get Item By Name
    public ResponseEntity<ResponseStructure<List<Item>>> getItemByName(String name) {
    	List<Item> items = itemRepository.findByItemName(name);
    	ResponseStructure<List<Item>> response = new ResponseStructure<>();
    	if(!items.isEmpty()) {
    		response.setStatusCode(HttpStatus.OK.value());
    	    response.setMessage("Items fetched successfully");
    	    response.setData(items);
    	    return new ResponseEntity<ResponseStructure<List<Item>>>(response, HttpStatus.OK);
    	} else {
    		throw new ResourceNotFoundException("Item not found");
    	}
    }
}
