package ms.kotlin.battleships.app.websocket

import ms.kotlin.battleships.app.security.AppUserEntity
import ms.kotlin.battleships.app.security.TokenService
import ms.kotlin.battleships.app.services.UserService
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class WebSocketAuthService {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var userService: UserService

    fun authenticate(token: String?): UsernamePasswordAuthenticationToken {

        try {
            val userId  = tokenService.extractIdFromToken(token) ?: throw NoSuchElementException()
            val user =  userService.loadUserByUserId(userId)
            return UsernamePasswordAuthenticationToken(user, null, user.authorities)
        }
        catch (ex: NoSuchElementException) {
            logger.error("Invalid token for websocket auth")
            throw ex
        }
    }

}