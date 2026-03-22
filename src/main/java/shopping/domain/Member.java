package shopping.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import shopping.exception.CustomExceptionEnum;
import shopping.exception.NotValidException;

@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String password;
	private String name;

	protected Member() {
	}

	private Member(Long id, String email, String password, String name) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public static Member create(String email, String password, String name) {
		return new Member(null, email, password, name);
	}

	public void validPassword(String encryptPassword) {
		if (this.password.equals(encryptPassword)) {
			return;
		}
		throw new NotValidException(CustomExceptionEnum.NOT_VALID_PASSWORD);
	}
}
