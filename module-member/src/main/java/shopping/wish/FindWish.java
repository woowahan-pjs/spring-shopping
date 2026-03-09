package shopping.wish;

import java.util.List;
import java.util.UUID;

public interface FindWish {

    List<Wish> execute(UUID memberId);
}
