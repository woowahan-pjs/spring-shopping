package shopping.wish.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.auth.LoginMember;
import shopping.member.domain.Member;
import shopping.wish.api.dto.WishResponse;
import shopping.wish.service.WishQueryService;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
@RequiredArgsConstructor
public class WishController {
    private final WishQueryService wishQueryService;

    @GetMapping
    public List<WishResponse> findAll(@LoginMember Member member) {
        return wishQueryService.findAll(member.getId()).stream()
                .map(WishResponse::from)
                .toList();
    }
}
