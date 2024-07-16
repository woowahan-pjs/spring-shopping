package shopping.user.application

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import shopping.common.util.JwtProvider
import shopping.user.domain.EncryptedPassword
import shopping.user.domain.User
import shopping.user.domain.UserAlreadyRegisteredException
import shopping.user.domain.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
) {
    @Transactional
    fun regist(request: UserRegistRequest): UserRegistResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyRegisteredException(request.email)
        }

        User(
            email = request.email,
            password = EncryptedPassword.from(request.password),
        ).let { userRepository.save(it) }

        val jwt = jwtProvider.createToken(request.email)

        return UserRegistResponse(accessToken = jwt)
    }
}
