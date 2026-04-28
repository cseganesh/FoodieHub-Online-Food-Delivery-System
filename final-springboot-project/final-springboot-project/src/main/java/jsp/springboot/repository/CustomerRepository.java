package jsp.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jsp.springboot.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	// 1. create customer
	boolean existsByEmail(String email);
    boolean existsByContact(String contact);
    
    // 6. get customer by contact
 	Optional<Customer> findByContact(String contact);
}
