package jsp.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.*;
import jsp.springboot.service.OrderItemService;

@RestController
@RequestMapping("/ordersItem")
public class OrderItemController {
	@Autowired
	private OrderItemService service;
	
	// 1. add item to existing order
	@PostMapping("/{orderId}/items")
	public ResponseEntity<ResponseStructure<Order>> addItemToOrder(@PathVariable Integer orderId, @RequestBody OrderItem item) {
		return service.addItemToOrder(orderId, item);
	}
	
	// 2️. Update order item quantity
    @PutMapping("/update/{orderItemId}/{quantity}")
    public ResponseEntity<ResponseStructure<OrderItem>> updateQuantity(@PathVariable Integer orderItemId, @PathVariable Integer quantity){
        return service.updateOrderItemQuantity(orderItemId, quantity);
    }
    
    // 3️. Remove order item
    @DeleteMapping("/delete/{orderItemId}")
    public ResponseEntity<ResponseStructure<String>> deleteOrderItem(@PathVariable Integer orderItemId){
        return service.removeOrderItem(orderItemId);
    }
    
    // 4️. Get order items of an order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseStructure<List<OrderItem>>> getItemsByOrder(@PathVariable Integer orderId){
        return service.getOrderItemsByOrder(orderId);
    }
}
