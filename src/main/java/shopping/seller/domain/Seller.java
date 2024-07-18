package shopping.seller.domain;

public class Seller {
    private final long id;
    private final String email;
    private final String name;
    private final String password;
    private final String birth;
    private final String address;
    private final String phone;

    public Seller(final long id, final String email, final String name, final String password, final String birth, final String address, final String phone) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.birth = birth;
        this.address = address;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getBirth() {
        return birth;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isSamePassword(final String password) {
        return this.password.equals(password);
    }
}