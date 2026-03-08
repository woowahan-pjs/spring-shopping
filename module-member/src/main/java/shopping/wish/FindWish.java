package shopping.wish;

import java.util.List;

public interface FindWish {

    List<Wish> execute(Long memberId);
}
