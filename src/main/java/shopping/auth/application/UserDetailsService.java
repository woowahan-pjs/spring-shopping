package shopping.auth.application;


public interface UserDetailsService {
    UserDetail loadUserByEmail(final String email);
}
