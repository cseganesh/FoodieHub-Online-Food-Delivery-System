package jsp.springboot.dto;

public class ResponseStructure<C> {
	private Integer statusCode;
	private String message;
	private C data;
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public C getData() {
		return data;
	}
	public void setData(C data) {
		this.data = data;
	}
}
