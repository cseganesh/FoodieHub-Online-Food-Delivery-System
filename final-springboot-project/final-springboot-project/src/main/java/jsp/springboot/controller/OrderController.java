package jsp.springboot.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Order;
import jsp.springboot.enums.OrderStatus;
import jsp.springboot.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    // 1. PLACE ORDER
    @PostMapping
    public ResponseEntity<ResponseStructure<Order>> placeOrder(@RequestBody Order order) {
        return service.placeOrder(order);
    }
    
    // 2. Get All Orders
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Order>>> getAllOrders() {
        return service.getAllOrders();
    }
    
    // 3. Get Order By ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Order>> getOrderById(@PathVariable Integer id) {
        return service.getOrderById(id);
    }
    
    // 4. Get Orders Of A Customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ResponseStructure<List<Order>>> getOrdersByCustomer(@PathVariable Integer customerId) {
        return service.getOrdersByCustomer(customerId);
    }
    
    // 5. Update Order Status
    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseStructure<Order>> updateOrderStatus(@PathVariable Integer id, @RequestParam OrderStatus status) {
        return service.updateOrderStatus(id, status);
    }
    
    // 6. Cancel Order
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ResponseStructure<Order>> cancelOrder(@PathVariable Integer id) {
        return service.cancelOrder(id);
    }
    
    // 7. Get Orders By Status
    // http://localhost:8080/orders/status?status=PLACED
    @GetMapping("/status")
    public ResponseEntity<ResponseStructure<List<Order>>> getOrdersByStatus(@RequestParam OrderStatus status) {
        return service.getOrdersByStatus(status);
    }
    
    // 8. Get Orders By Date
    // http://localhost:8080/orders/date?start=2026-03-20T00:00:00&end=2026-03-23T23:59:59
    @GetMapping("/date")
    public ResponseEntity<ResponseStructure<List<Order>>> getOrdersByDate(@RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        return service.getOrdersByDate(start, end);
    }
    
    // 9. Get Orders Between Total Amount Range
    //http://localhost:8080/orders/amount?min=100&max=500
    @GetMapping("/amount")
    public ResponseEntity<ResponseStructure<List<Order>>> getOrdersByAmount(@RequestParam Double min, @RequestParam Double max) {
        return service.getOrdersByAmount(min, max);
    }
    
    // 10. Get Orders Of A Particular Restaurant
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<ResponseStructure<List<Order>>> getOrdersByRestaurant(@PathVariable Integer restaurantId) {
        return service.getOrdersByRestaurant(restaurantId);
    }
}