package shopping.member.application.command;

import static shopping.member.domain.MemberErrorMessage.LOGIN_FAILED;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import shopping.common.auth.PasswordEncoder;
import shopping.common.auth.TokenProvider;
import shopping.member.application.dto.MemberLoginRequest;
import shopping.member.application.dto.MemberLoginResponse;
import shopping.common.exception.ApiException;
import shopping.common.exception.ErrorType;
import shopping.member.domain.MemberEntity;
import shopping.member.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberLoginUseCase {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public MemberLoginResponse execute(MemberLoginRequest request) {
        MemberEntity member = getMemberEntity(request);
        validPassword(request.password(), member.getPassword());
        String token = tokenProvider.generateToken(member.getId(), member.getRole().name());
        return new MemberLoginResponse(token);
    }

    private MemberEntity getMemberEntity(MemberLoginRequest request) {
        return memberRepository.findByEmail(request.email())
            .orElseThrow(
                () -> new ApiException(LOGIN_FAILED.getDescription(), ErrorType.INVALID_PARAMETER,
                    HttpStatus.UNAUTHORIZED));
    }

    private void validPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ApiException(LOGIN_FAILED.getDescription(), ErrorType.INVALID_PARAMETER,
                HttpStatus.UNAUTHORIZED);
        }
    }
}
