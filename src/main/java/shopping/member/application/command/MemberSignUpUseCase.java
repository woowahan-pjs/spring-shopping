package shopping.member.application.command;

import static shopping.member.domain.MemberErrorMessage.ALREADY_EXISTS_EMAIL;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.auth.PasswordEncoder;
import shopping.common.auth.TokenProvider;
import shopping.member.application.dto.MemberSignUpRequest;
import shopping.member.application.dto.MemberSignUpResponse;
import shopping.common.exception.ApiException;
import shopping.common.exception.ErrorType;
import shopping.member.domain.MemberEntity;
import shopping.member.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberSignUpUseCase {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public MemberSignUpResponse execute(MemberSignUpRequest request) {
        memberRepository.findByEmail(request.email()).ifPresent(m -> {
            throw new ApiException(ALREADY_EXISTS_EMAIL.getDescription(),
                ErrorType.INVALID_PARAMETER, HttpStatus.CONFLICT);
        });

        String encodedPassword = passwordEncoder.encode(request.password());
        MemberEntity member = memberRepository.save(
            new MemberEntity(request.email(), encodedPassword));
        String token = tokenProvider.generateToken(member.getId(), member.getRole().name());
        return new MemberSignUpResponse(token);
    }
}
