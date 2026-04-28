package jsp.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Payment;
import jsp.springboot.enums.PaymentMethod;
import jsp.springboot.enums.PaymentStatus;
import jsp.springboot.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService service;

    // 1. Get Payment by ID
    @GetMapping("/{paymentId}")
    public ResponseEntity<ResponseStructure<Payment>> getPaymentById(@PathVariable int paymentId) {
        return service.getPaymentById(paymentId);
    }
    
    // 2. Get Payment by Status
    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseStructure<List<Payment>>> getPaymentByStatus(@PathVariable PaymentStatus status) {
        return service.getPaymentByStatus(status);
    }
    
    // 3. Get Payment by Payment Method
    @GetMapping("/method/{method}")
    public ResponseEntity<ResponseStructure<List<Payment>>> getPaymentByMethod(@PathVariable PaymentMethod method) {
        return service.getPaymentByMethod(method);
    }
    
    // 4. Update Payment Status
    @PutMapping("/{paymentId}/{status}")
    public ResponseEntity<ResponseStructure<Payment>> updatePaymentStatus(@PathVariable int paymentId, @PathVariable PaymentStatus status) {
        return service.updatePaymentStatus(paymentId, status);
    }
}