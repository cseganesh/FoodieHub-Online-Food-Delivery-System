package jsp.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Order;
import jsp.springboot.entity.Payment;
import jsp.springboot.enums.PaymentMethod;
import jsp.springboot.enums.PaymentStatus;
import jsp.springboot.exception.BusinessValidationException;
import jsp.springboot.exception.ResourceNotFoundException;
import jsp.springboot.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // 1. Get Payment by ID
    public ResponseEntity<ResponseStructure<Payment>> getPaymentById(int paymentId) {

        // Check payment exists
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        //  Check order linked
        Order order = payment.getOrder();
        if (order == null)
            throw new BusinessValidationException("Payment is not linked to any order");

        //  Validate amount
        if (Math.abs(payment.getAmount() - order.getTotalAmount()) > 0.01) {
            throw new BusinessValidationException("Payment amount mismatch with order total");
        }

        ResponseStructure<Payment> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Payment fetched successfully");
        response.setData(payment);
        return new ResponseEntity<ResponseStructure<Payment>>(response, HttpStatus.CREATED);
    }
    
    // 2. Get Payment by Status
    public ResponseEntity<ResponseStructure<List<Payment>>> getPaymentByStatus(PaymentStatus status) {
        List<Payment> payments = paymentRepository.findByPaymentStatus(status);
        if (payments.isEmpty()) {
            throw new ResourceNotFoundException("No payments found with status: " + status);
        }

        ResponseStructure<List<Payment>> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Payments fetched by status successfully");
        response.setData(payments);

        return new ResponseEntity<ResponseStructure<List<Payment>>>(response, HttpStatus.OK);
    }
    
    // 3. Get Payment by Payment Method
    public ResponseEntity<ResponseStructure<List<Payment>>> getPaymentByMethod(PaymentMethod method) {
        List<Payment> payments = paymentRepository.findByPaymentMethod(method);

        if (payments.isEmpty()) {
            throw new ResourceNotFoundException("No payments found with method: " + method);
        }

        ResponseStructure<List<Payment>> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Payments fetched by payment method successfully");
        response.setData(payments);

        return new ResponseEntity<ResponseStructure<List<Payment>>>(response, HttpStatus.OK);
    }
    
    // 4. Update Payment Status
    public ResponseEntity<ResponseStructure<Payment>> updatePaymentStatus(int paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        payment.setPaymentStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);

        ResponseStructure<Payment> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Payment status updated successfully");
        response.setData(updatedPayment);

        return new ResponseEntity<ResponseStructure<Payment>>(response, HttpStatus.OK);
    }
}