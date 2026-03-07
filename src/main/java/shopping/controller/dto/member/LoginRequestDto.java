package shopping.controller.dto.member;

public class LoginRequestDto {
	private String email;
	private String encryptPassword;

	public LoginRequestDto(String email, String encryptPassword) {
		this.email = email;
		this.encryptPassword = encryptPassword;
	}

	public String getEmail() {
		return email;
	}

	public String getEncryptPassword() {
		return encryptPassword;
	}
}
