package shopping.exception;

public enum CustomExceptionEnum {
	NOT_VALID_NAME_LENGTH("1000", "글자수는 공백 포함 15자 이하입니다."),
	NOT_VALID_SPECIAL_CHARACTERS("1001", "사용 불가능한 특수문자가 포함되어 있습니다."),
	NOT_VALID_LANGUAGE("1002", "비속어가 포함된 글자는 사용할 수 없습니다."),
	NOT_EXIST_MEMBER("1003", "존재하지 않는 회원입니다."),
	NOT_VALID_PASSWORD("1004", "비밀번호가 일치하지 않습니다."),
	NOT_EXIST_PRODUCT("1005", "존재하지 않은 상품입니다.");
	;
	private final String code;
	private final String message;

	CustomExceptionEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
