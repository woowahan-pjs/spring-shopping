package shopping.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shopping.auth.AdminInterceptor;
import shopping.auth.AuthInterceptor;
import shopping.auth.JwtTokenProvider;
import shopping.member.service.MemberService;
import shopping.product.service.ProductService;
import shopping.wishlist.service.WishlistService;

@WebMvcTest
@Import({AdminInterceptor.class, AuthInterceptor.class})
@AutoConfigureRestDocs
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected JwtTokenProvider provider;

    @MockitoBean
    protected MemberService memberService;

    @MockitoBean
    protected ProductService productService;

    @MockitoBean
    protected WishlistService wishlistService;
}
