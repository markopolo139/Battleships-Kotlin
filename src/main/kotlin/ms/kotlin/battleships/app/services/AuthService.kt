package ms.kotlin.battleships.app.services

import ms.kotlin.battleships.app.security.AppUserEntity
import ms.kotlin.battleships.app.security.TokenService
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthService {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    fun authenticate(username: String?, password: String?): String = try {
        val auth = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        tokenService.createAuthenticationToken((auth.principal as AppUserEntity).id)
    }
    catch (ex: BadCredentialsException) {
        logger.error("Attempt of obtaining token with invalid credentials has been made for username $username")
        throw ex
    }

}