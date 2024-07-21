package shopping.seller.infrastructure.persistence;

import jakarta.persistence.*;

@Table(name = "sellers")
@Entity
public class SellerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String name;
    private String address;
    private String birth;
    private String phone;
    private String password;

    public SellerEntity() {
    }

    public SellerEntity(final String email,
                        final String name,
                        final String address,
                        final String birth,
                        final String phone,
                        final String password) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.birth = birth;
        this.phone = phone;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBirth() {
        return birth;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}
