package jsp.springboot.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.*;
import jsp.springboot.exception.*;
import jsp.springboot.repository.*;

@Service
public class OrderItemService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    // 1. Add Item to Existing Order
    public ResponseEntity<ResponseStructure<Order>> addItemToOrder(int orderId, OrderItem newItem) {

        // Order must exist
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        //  Quantity must be >= 1
        if (newItem.getQuantity() < 1)
            throw new InvalidInputException("Quantity must be minimum 1");

        //  Fetch item from DB
        Item menuItem = menuItemRepository.findById(
                newItem.getItem().getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        // Item must be available
        if (!menuItem.isAvailable())
            throw new BusinessValidationException("Item not available");

        //  Subtotal = price * quantity
        double subTotal = menuItem.getPrice() * newItem.getQuantity();

        newItem.setSubTotal(subTotal);
        newItem.setItem(menuItem);
        newItem.setOrder(order);

        //  Add item to order
        List<OrderItem> items = order.getOrderItems();
        items.add(newItem);

        //  Recalculate total
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubTotal();
        }
        order.setTotalAmount(total);

        // Update payment
        if (order.getPayment() != null) {
            order.getPayment().setAmount(total);
        }

  
        ResponseStructure<Order> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Item added successfully");
        response.setData(orderRepository.save(order));

        return new ResponseEntity<ResponseStructure<Order>>(response, HttpStatus.CREATED);
    }
    
    // 2️. Update item quantity
    public ResponseEntity<ResponseStructure<OrderItem>> updateOrderItemQuantity(Integer orderItemId, Integer quantity){
        if(quantity < 1)
            throw new InvalidInputException("Quantity must be minimum 1");

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found"));
        orderItem.setQuantity(quantity);

        double subTotal = orderItem.getItem().getPrice() * quantity;
        orderItem.setSubTotal(subTotal);
        Order order = orderItem.getOrder();

        double total = 0;
        for(OrderItem item : order.getOrderItems()){
            total += item.getSubTotal();
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        ResponseStructure<OrderItem> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Order item quantity updated");
        response.setData(orderItem);

        return new ResponseEntity<ResponseStructure<OrderItem>>(response,HttpStatus.OK);
    }
    
    // 3️. Remove item from order
    public ResponseEntity<ResponseStructure<String>> removeOrderItem(Integer orderItemId){
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found"));

        Order order = orderItem.getOrder();
        order.getOrderItems().remove(orderItem);
        orderItemRepository.delete(orderItem);

        double total = 0;
        for(OrderItem item : order.getOrderItems()){
            total += item.getSubTotal();
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        ResponseStructure<String> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Order item removed successfully");
        response.setData("Deleted");

        return new ResponseEntity<ResponseStructure<String>>(response,HttpStatus.OK);
    }
    
    // 4️. Get order items of an order
    public ResponseEntity<ResponseStructure<List<OrderItem>>> getOrderItemsByOrder(Integer orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        List<OrderItem> items = order.getOrderItems();

        ResponseStructure<List<OrderItem>> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Order items fetched successfully");
        response.setData(items);

        return new ResponseEntity<ResponseStructure<List<OrderItem>>>(response,HttpStatus.OK);
    }
}

