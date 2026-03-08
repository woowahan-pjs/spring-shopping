package shopping.wishlist.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.Comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shopping.infra.orm.AuditInformation;

@Getter
@Entity
@Table(name = "wishlist")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishList extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @Comment("회원 고유 ID")
    @Column(name = "user_id", unique = true, nullable = false, columnDefinition = "bigint")
    private Long userId;

    @OneToMany(mappedBy = "wishListId", cascade = CascadeType.PERSIST)
    private List<WishListItem> items = new ArrayList<>();

    public static WishList generate() {
        return new WishList();
    }
}
