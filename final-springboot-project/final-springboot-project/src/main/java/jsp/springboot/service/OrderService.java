package jsp.springboot.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.*;
import jsp.springboot.enums.*;
import jsp.springboot.exception.*;
import jsp.springboot.repository.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    // ===============================
    // 1. PLACE ORDER
    // ===============================
    public ResponseEntity<ResponseStructure<Order>> placeOrder(Order order) {

        //  Customer validation
        if (order.getCustomer() == null)
            throw new InvalidInputException("Customer is required");

        Customer customer = customerRepository.findById(
                order.getCustomer().getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Order items validation
        List<OrderItem> items = order.getOrderItems();
        if (items == null || items.isEmpty())
            throw new InvalidInputException("Order must have at least one item");

        double total = 0;

        for (OrderItem item : items) {

            if (item.getQuantity() < 1)
                throw new InvalidInputException("Quantity must be minimum 1");

            Item menuItem = menuItemRepository.findById(
                    item.getItem().getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

            if (!menuItem.isAvailable())
                throw new BusinessValidationException("Item not available");

            double subTotal = menuItem.getPrice() * item.getQuantity();

            item.setSubTotal(subTotal);
            item.setItem(menuItem);
            item.setOrder(order);

            total += subTotal;
        }

        //  Payment validation
        Payment payment = order.getPayment();
        if (payment == null)
            throw new PaymentException("Payment is required");

        if (payment.getPaymentMethod() == null) {
            payment.setPaymentMethod(PaymentMethod.UPI);
        }

        if (payment.getPaymentStatus() == null) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
        }
        
        payment.setAmount(total);
        payment.setOrder(order);

        // Order setup
        order.setCustomer(customer);
        order.setTotalAmount(total);
        order.setOrderDateTime(LocalDateTime.now());

        if (order.getStatus() == null)
            order.setStatus(OrderStatus.PLACED);

        // Save
        Order savedOrder = orderRepository.save(order);

        // Response
        ResponseStructure<Order> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Order Placed Successfully");
        response.setData(savedOrder);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    // ===============================
    // 2. Get All Orders
    // ===============================
    public ResponseEntity<ResponseStructure<List<Order>>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty())
            throw new ResourceNotFoundException("No orders found");

        ResponseStructure<List<Order>> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Orders fetched successfully");
        response.setData(orders);
        return new ResponseEntity<ResponseStructure<List<Order>>>(response, HttpStatus.OK);
    }
    
    // ===============================
    // 3. Get Order By ID
    // ===============================
    public ResponseEntity<ResponseStructure<Order>> getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        ResponseStructure<Order> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Order fetched successfully");
        response.setData(order);
        return new ResponseEntity<ResponseStructure<Order>>(response, HttpStatus.OK);
    }
    
    // ===============================
    // 4. Get Orders Of A Customer
    // ===============================
    public ResponseEntity<ResponseStructure<List<Order>>> getOrdersByCustomer(Integer customerId) {
        List<Order> orders = orderRepository.findByCustomerCustomerId(customerId);
        if (orders.isEmpty())
            throw new ResourceNotFoundException("No orders found for this customer");

        ResponseStructure<List<Order>> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Customer orders fetched");
        response.setData(orders);

        return new ResponseEntity<ResponseStructure<List<Order>>>(response, HttpStatus.OK);
    }
    
    // ===============================
    // 5. Update Order Status
    // ===============================
    public ResponseEntity<ResponseStructure<Order>> updateOrderStatus(Integer id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        ResponseStructure<Order> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Order status updated");
        response.setData(updatedOrder);

        return new ResponseEntity<ResponseStructure<Order>>(response, HttpStatus.OK);
    }
    
    // ===============================
    // 6. Cancel Order
    // ===============================
    public ResponseEntity<ResponseStructure<Order>> cancelOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.DELIVERED)
            throw new OrderStateException("Delivered order cannot be cancelled");

        order.setStatus(OrderStatus.CANCELLED);
        Order updatedOrder = orderRepository.save(order);

        ResponseStructure<Order> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Order cancelled successfully");
        response.setData(updatedOrder);

        return new ResponseEntity<ResponseStructure<Order>>(response, HttpStatus.OK);
    }
    
    // ===============================
    // 7. Get Orders By Status
    // ===============================
    public ResponseEntity<ResponseStructure<List<Order>>> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        if (orders.isEmpty())
            throw new ResourceNotFoundException("No orders found");

        ResponseStructure<List<Order>> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Orders fetched by status");
        response.setData(orders);
        return new ResponseEntity<ResponseStructure<List<Order>>>(response, HttpStatus.OK);
    }
    
    // ===============================
    // 8. Get Orders By Date
    // ===============================
    public ResponseEntity<ResponseStructure<List<Order>>> getOrdersByDate(LocalDateTime start, LocalDateTime end) {
        List<Order> orders = orderRepository.findByOrderDateTimeBetween(start, end);

        if (orders.isEmpty())
            throw new ResourceNotFoundException("No orders found");
        ResponseStructure<List<Order>> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Orders fetched by date");
        response.setData(orders);

        return new ResponseEntity<ResponseStructure<List<Order>>>(response, HttpStatus.OK);
    }
    
    // ===============================
    // 9. Get Orders Between Total Amount Range
    // ===============================
    public ResponseEntity<ResponseStructure<List<Order>>> getOrdersByAmount(Double min, Double max) {
        List<Order> orders = orderRepository.findByTotalAmountBetween(min, max);

        if (orders.isEmpty())
            throw new ResourceNotFoundException("No orders found");

        ResponseStructure<List<Order>> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Orders fetched by amount range");
        response.setData(orders);
        return new ResponseEntity<ResponseStructure<List<Order>>>(response, HttpStatus.OK);
    }
    
    // ===============================
    // 10. Get Orders Of A Particular Restaurant
    // ===============================
    public ResponseEntity<ResponseStructure<List<Order>>> getOrdersByRestaurant(Integer restaurantId) {
        List<Order> orders = orderRepository.findByOrderItemsItemRestaurantRestaurantId(restaurantId);

        if (orders.isEmpty())
            throw new ResourceNotFoundException("No orders found for this restaurant");

        ResponseStructure<List<Order>> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Restaurant orders fetched");
        response.setData(orders);

        return new ResponseEntity<ResponseStructure<List<Order>>>(response, HttpStatus.OK);
    }
}