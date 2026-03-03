package shopping.infra.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import shopping.auth.domain.Role;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPrincipal implements UserDetails {

    private static final String ROLE_PREFIX = "ROLE_";

    private Long id;

    private String email;

    private Role role;

    public static UserPrincipal generate(final Long id, final String email, final Role role) {
        UserPrincipal principal = new UserPrincipal();

        principal.id = id;
        principal.email = email;
        principal.role = role;

        return principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ROLE_PREFIX + role.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public String getUserIdToString() {
        return this.id.toString();
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }
}
