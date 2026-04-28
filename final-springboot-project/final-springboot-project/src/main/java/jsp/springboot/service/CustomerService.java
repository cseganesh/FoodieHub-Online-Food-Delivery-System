package jsp.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Customer;
import jsp.springboot.exception.BusinessValidationException;
import jsp.springboot.exception.InvalidInputException;
import jsp.springboot.exception.ResourceNotFoundException;
import jsp.springboot.repository.CustomerRepository;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository repository;
	
	// 1. create customer
	public ResponseEntity<ResponseStructure<Customer>> createCustomer(Customer customer) {
		if(customer.getContact().length()!=10) 
		    throw new InvalidInputException("Contact must be 10 digits");

		if(repository.existsByEmail(customer.getEmail()))
		    throw new BusinessValidationException("Email already exists");

		if(repository.existsByContact(customer.getContact()))
		    throw new BusinessValidationException("Contact already exists");

	    ResponseStructure<Customer> response = new ResponseStructure<Customer>();
	    response.setStatusCode(HttpStatus.CREATED.value());
	    response.setMessage("Create customer Successfully!");
	    response.setData(repository.save(customer));

	    return new ResponseEntity<ResponseStructure<Customer>>(response, HttpStatus.CREATED);
	}
	
	// 2. get all customers
	public ResponseEntity<ResponseStructure<List<Customer>>> getAllCustomers() {
		List<Customer> customers = repository.findAll();
		if(!customers.isEmpty()) {
			ResponseStructure<List<Customer>> response = new ResponseStructure<>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Customers fetched successfully");
			response.setData(customers);
			return new ResponseEntity<ResponseStructure<List<Customer>>>(response, HttpStatus.OK);
		}
		throw new ResourceNotFoundException("No Customers available in the DB");
	}
	
	// 3. Get Customer By Id
	public ResponseEntity<ResponseStructure<Customer>> getCustomerById(Integer id) {
		Optional<Customer> opt = repository.findById(id);
		if(opt.isEmpty()) {
			throw new ResourceNotFoundException("Customer not fuond with id: " + id);
		}
		ResponseStructure<Customer> response = new ResponseStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Customer fetched successfully");
		response.setData(opt.get());
		return new ResponseEntity<ResponseStructure<Customer>>(response, HttpStatus.OK);
	}
	
	// 4. Update Customer
	public ResponseEntity<ResponseStructure<Customer>> updateCustomer(Integer id, Customer customer) {
	    Optional<Customer> opt = repository.findById(id);
	    ResponseStructure<Customer> response = new ResponseStructure<>();
	    if(opt.isPresent()) {
	        customer.setCustomerId(id);
	        Customer updatedCustomer = repository.save(customer);
	        response.setStatusCode(HttpStatus.OK.value());
	        response.setMessage("Customer updated successfully");
	        response.setData(updatedCustomer);
	        return new ResponseEntity<ResponseStructure<Customer>>(response, HttpStatus.OK);
	    } else {
	        throw new ResourceNotFoundException("Customer not found with id: " + id);
	    }
	}
	
	// 5. delete customer
	public ResponseEntity<ResponseStructure<String>> deleteCustomer(Integer id) {
		Optional<Customer> opt = repository.findById(id);
		ResponseStructure<String> response = new ResponseStructure<>();
		if(opt.isPresent()) {
			repository.deleteById(id);
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Customer deleted successfully");
			response.setData("Deleted");
			return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.OK);
		}
		throw new ResourceNotFoundException("Customer not found with id: " + id);
	}
	
	// 6. get by contact 
	public ResponseEntity<ResponseStructure<Customer>> getCustomerByContact(String contact) {
		Optional<Customer> opt = repository.findByContact(contact);
		if (opt.isEmpty()) {
			throw new ResourceNotFoundException("Customer not found with contact: " + contact);
		}
		ResponseStructure<Customer> response = new ResponseStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Customer fetched successfully");
		response.setData(opt.get());
		return new ResponseEntity<ResponseStructure<Customer>>(response, HttpStatus.OK);
	}
}
