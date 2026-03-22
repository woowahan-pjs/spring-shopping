package shopping.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.auth.domain.DuplicateEmailException;
import shopping.auth.domain.InvalidUserException;
import shopping.auth.domain.User;
import shopping.auth.dto.RegisterRequest;
import shopping.auth.dto.UserResponse;
import shopping.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입 요청을 처리하고 신규 회원 정보를 저장합니다. 중복 이메일이 존재하는 경우 예외를 발생시킵니다.
     *
     * @param request 회원 가입 요청 정보를 담고 있는 RegisterRequest 객체입니다. 이 객체에는 이메일, 비밀번호, 권한 정보가 포함됩니다.
     * @return 저장된 회원의 고유 ID를 반환합니다.
     */
    @Transactional
    public Long register(final RegisterRequest request) {
        // 1. 중복 이메일 체크
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException();
        }

        // 2. 회원 가입
        final User user = userRepository.save(User.generate(request, passwordEncoder.encode(request.password())));

        return user.getId();
    }

    /**
     * 이메일과 비밀번호를 사용하여 활성화된 회원 정보를 조회합니다. 비밀번호가 일치하지 않거나 사용자가 존재하지 않을 경우 예외를 발생시킵니다.
     *
     * @param email 조회할 회원의 이메일 주소입니다.
     * @param password 조회할 회원의 비밀번호입니다.
     * @return 조회된 회원의 정보(UserResponse)를 반환합니다.
     */
    @Transactional(readOnly = true)
    public UserResponse getUser(final String email, final String password) {
        final User user = userRepository.findByEmailAndIsUse(email, true)
                .orElseThrow(InvalidUserException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidUserException();
        }

        return UserResponse.from(user.getId(), user.getEmail(), user.getRole());
    }
}
