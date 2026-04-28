package jsp.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jsp.springboot.dto.ResponseStructure;

@ControllerAdvice
public class GlobalExceptionHandler {

	// ResourceNotFoundException
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleResourceNotFoundException(ResourceNotFoundException exception) {

		ResponseStructure<String> response = new ResponseStructure<>();

		response.setStatusCode(HttpStatus.NOT_FOUND.value());
		response.setMessage(exception.getMessage());
		response.setData("Failure");

		return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.NOT_FOUND);
	}
	
	// InvalidInputException
	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<ResponseStructure<String>> handleInvalidInputException(InvalidInputException exception) {
		
		ResponseStructure<String> response = new ResponseStructure<>();
		
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(exception.getMessage());
		response.setData("Failure");
		
		return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.BAD_REQUEST);
	}
	
	// BusinessValidationException
	@ExceptionHandler(BusinessValidationException.class)
	public ResponseEntity<ResponseStructure<String>> handleBusinessValidationException(BusinessValidationException exception) {
		
		ResponseStructure<String> response = new ResponseStructure<>();
		
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(exception.getMessage());
		response.setData("Failure");
		
		return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.BAD_REQUEST);
	}
	
	// UnauthorizedActionException
	@ExceptionHandler(UnauthorizedActionException.class)
	public ResponseEntity<ResponseStructure<String>> handleUnauthorizedActionException(UnauthorizedActionException exception) {
		
		ResponseStructure<String> response = new ResponseStructure<>();
		
		response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		response.setMessage(exception.getMessage());
		response.setData("Failure");
		
		return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.UNAUTHORIZED);
	}
	
	// PaymentException
	@ExceptionHandler(PaymentException.class)	
	public ResponseEntity<ResponseStructure<String>> handlePaymentException(PaymentException exception) {
		
		ResponseStructure<String> response = new ResponseStructure<>();
		
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(exception.getMessage());
		response.setData("Failure");
		
		return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.BAD_REQUEST);
	}
	
	// OrderStateException
	@ExceptionHandler(OrderStateException.class)
	public ResponseEntity<ResponseStructure<String>> handleOrderStateException(OrderStateException exception) {
		
		ResponseStructure<String> response = new ResponseStructure<>();
		
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(exception.getMessage());
		response.setData("Failure");
		
		return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.BAD_REQUEST);
	}
}