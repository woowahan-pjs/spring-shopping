package shopping.wish.api.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.config.LoginMember;
import shopping.common.response.PageResponse;
import shopping.member.domain.Member;
import shopping.wish.api.query.dto.WishResponse;
import shopping.wish.service.WishQueryService;

@RestController
@RequestMapping("/api/wishes")
@RequiredArgsConstructor
public class WishQueryController {
    private final WishQueryService wishQueryService;

    @GetMapping
    public PageResponse<WishResponse> findAll(
            @LoginMember Member member,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return PageResponse.from(
                wishQueryService.findWishWithPage(member.getId(), pageable)
                                .map(WishResponse::from)
        );
    }
}
