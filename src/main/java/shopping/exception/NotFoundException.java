package shopping.exception;

public class NotFoundException extends RuntimeException {
	private final String code;
	public NotFoundException(CustomExceptionEnum customExceptionEnum) {
		super(customExceptionEnum.getMessage());
		this.code = customExceptionEnum.getCode();
	}
	public String getCode() {
		return code;
	}
}
