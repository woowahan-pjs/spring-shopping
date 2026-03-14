package shopping.wish.domain;

import java.util.List;
import java.util.UUID;

public interface FindWish {

    List<Wish> execute(UUID memberId);
}
