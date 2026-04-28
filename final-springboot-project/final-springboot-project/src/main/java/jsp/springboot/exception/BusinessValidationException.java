package jsp.springboot.exception;

public class BusinessValidationException extends RuntimeException{
	public BusinessValidationException(String message) {
		super(message);
	}
}
