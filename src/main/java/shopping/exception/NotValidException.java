package shopping.exception;

public class NotValidException extends RuntimeException {

	private final String code;

	public NotValidException(CustomExceptionEnum customExceptionEnum) {
		super(customExceptionEnum.getMessage());
		this.code = customExceptionEnum.getCode();
	}

	public String getCode() {
		return code;
	}
}
