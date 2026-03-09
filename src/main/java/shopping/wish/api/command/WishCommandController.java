package shopping.wish.api.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shopping.auth.LoginMember;
import shopping.member.domain.Member;
import shopping.wish.api.command.dto.WishAddRequest;
import shopping.wish.api.command.dto.WishAddResponse;
import shopping.wish.service.WishCommandService;
import shopping.wish.service.dto.WishAddInput;

@RestController
@RequestMapping("/api/wishes")
@RequiredArgsConstructor
public class WishCommandController {
    private final WishCommandService wishCommandService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WishAddResponse add(@LoginMember Member member, @RequestBody WishAddRequest request) {
        return WishAddResponse.from(wishCommandService.add(new WishAddInput(member.getId(), request.productId())));
    }

    @DeleteMapping("/{wishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@LoginMember Member member, @PathVariable Long wishId) {
        wishCommandService.delete(wishId, member.getId());
    }
}
