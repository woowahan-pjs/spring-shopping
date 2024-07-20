package shopping.auth.application;


public class MockUserDetailsService implements UserDetailsService {
    private final String userPassword;

    public MockUserDetailsService(final String userPassword) {
        this.userPassword = userPassword;
    }

    public UserDetail loadUserByEmail(final String email) {
        return new UserDetail(1L, email, userPassword);
    }

}
