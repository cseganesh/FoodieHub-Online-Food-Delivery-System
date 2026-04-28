package jsp.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Item;
import jsp.springboot.service.MenuItemService;

@RestController
@RequestMapping("/menuItem")
public class MenuItemController {

    @Autowired 
    private MenuItemService service;

    // 1. Add Menu Item
    @PostMapping
    public ResponseEntity<ResponseStructure<Item>> addMenuItem(@RequestBody Item item) {
        return service.addMenuItem(item);
    }
    
    // 2. Get All Menu Item
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Item>>> getAllMenuItems(){
        return service.getAllMenuItems();
    }
    
    // 3. Get Menu Item By ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Item>> getMenuItemById(@PathVariable Integer id){
        return service.getMenuItemById(id);
    }
    
    // 4. Update Price
    // http://localhost:8080/menuItem/3/price?price=200
    @PutMapping("/{id}/price")
    public ResponseEntity<ResponseStructure<Item>> updatePrice(@PathVariable Integer id, @RequestParam Double price){
        return service.updatePrice(id, price);
    }
    
    // 5. Delete Menu Item
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteMenuItem(@PathVariable Integer id){
        return service.deleteMenuItem(id);
    }
    
    // 6. Get Items Greater Than Price
    // http://localhost:8080/menuItem/price?price=200
    @GetMapping("/price")
    public ResponseEntity<ResponseStructure<List<Item>>> getItemsGreaterThanPrice(@RequestParam Double price){
        return service.getItemsGreaterThanPrice(price);
    }
    
    // 7. Get Item By Name
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseStructure<List<Item>>> getItemByName(@PathVariable String name){
        return service.getItemByName(name);
    }
}