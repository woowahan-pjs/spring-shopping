package shopping.user.application

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import shopping.common.auth.JwtProvider
import shopping.user.domain.EncryptedPassword
import shopping.user.domain.PasswordMismatchException
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

    fun login(request: UserLoginRequest): UserLoginResponse {
        val user =
            userRepository.findByEmail(request.email)
                ?: throw UserNotFoundException.fromEmail(request.email)

        if (user.password != EncryptedPassword.from(request.password)) {
            throw PasswordMismatchException()
        }

        val jwt = jwtProvider.createToken(request.email)

        return UserLoginResponse(accessToken = jwt)
    }
}
