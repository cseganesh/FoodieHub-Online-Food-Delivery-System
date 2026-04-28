package jsp.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Customer;
import jsp.springboot.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	@Autowired
	private CustomerService service;
	
	// 1. Create Customer
	@PostMapping
	public ResponseEntity<ResponseStructure<Customer>> createCustomer(@RequestBody Customer customer) {
		return service.createCustomer(customer);
	}
	
	// 2. get all customers
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Customer>>> getAllCustomers() {
		return service.getAllCustomers();
	}
	
	// 3. Get Customer By Id
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Customer>> getCustomerById(@PathVariable Integer id) {
		return service.getCustomerById(id);
	}
	
	// 4. Update Customer
	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<Customer>> updateCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
		return service.updateCustomer(id, customer);
	}
	
	// 5. Delete Customer
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteCustomer(@PathVariable Integer id) {
		return service.deleteCustomer(id);
	}
	
	// 6. Get Customer By Contact
	@GetMapping("/contact/{contact}")
	public ResponseEntity<ResponseStructure<Customer>> getCustomerByContact(@PathVariable String contact) {
		return service.getCustomerByContact(contact);
	}
}
