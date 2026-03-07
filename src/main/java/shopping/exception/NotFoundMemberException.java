package shopping.exception;

public class NotFoundMemberException extends RuntimeException {
	private final String code;
	public NotFoundMemberException(CustomExceptionEnum customExceptionEnum) {
		super(customExceptionEnum.getMessage());
		this.code = customExceptionEnum.getCode();
	}
	public String getCode() {
		return code;
	}
}
