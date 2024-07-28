package shopping.utils.fixture;

import java.util.List;

public class ProductFixture {
    public static final String NAME = "무제";
    public static final String INVALID_NAME = "이상품은영국에서부터시작하여세계로퍼져나가고있는상품";
    public static final String PROFANITY_NAME = "Fucking Product";
    public static final String INVALID_EXPRESS_NAME = "hello#world";
    public static final String THUMBNAIL_IMAGE_URL = "https://woozicdn.test/image.png";
    public static final long AMOUNT = 10000L;
    public static final Long SUB_CATEGORY_ID = 1L;
    public static final Long SHOP_ID = 1L;
    public static final Long SELLER_ID = 1L;
    public static final List<String> DETAILED_IMAGE_URLS = List.of(
            "https://woozicdn.test/detailed/image1.png",
            "https://woozicdn.test/detailed/image2.png",
            "https://woozicdn.test/detailed/image3.png",
            "https://woozicdn.test/detailed/image4.png"
    );
}
