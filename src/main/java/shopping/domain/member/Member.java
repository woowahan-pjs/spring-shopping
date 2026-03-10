package shopping.domain.member;

import jakarta.persistence.*;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    protected Member() {}

    public Member(String email, String password) {
        this(email, password, Role.CONSUMER);
    }

    public Member(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public static Member create(String email, String password) {
        return new Member(email, password, Role.CONSUMER);
    }
}
