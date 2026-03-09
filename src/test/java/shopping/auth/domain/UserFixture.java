package shopping.auth.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {

    public static User fixture(final Long id, final String email, final String password, final Role role) {
        User user = new User();

        ReflectionTestUtils.setField(user, "id", id);
        ReflectionTestUtils.setField(user, "email", email);
        ReflectionTestUtils.setField(user, "password", password);
        ReflectionTestUtils.setField(user, "role", role);
        ReflectionTestUtils.setField(user, "isUse", true);

        return user;
    }
}
