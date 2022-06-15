package ms.kotlin.battleships.app.security

import io.jsonwebtoken.*
import ms.kotlin.battleships.app.persistence.repositories.UserRepository
import ms.kotlin.battleships.app.services.UserService
import ms.kotlin.battleships.business.value.GameType
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.http.HttpHeaders
import java.util.*
import javax.servlet.http.HttpServletRequest
import io.jsonwebtoken.security.SignatureException

@Service
class TokenService {

    companion object {
        private val logger = LogManager.getLogger()
    }

    enum class Claims(val value: String) {
        PASSWORD_RECOVERY("pwr"),
        JOIN_GAME("jg")
    }

    @Autowired
    private lateinit var jwtConf: JwtConf

    @Autowired
    private lateinit var userService: UserService

    private val currentRequest: HttpServletRequest?
        get() = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request

    fun createAuthenticationToken(userId: Int): String =
        Jwts
            .builder()
            .configure(jwtConf.validity)
            .setSubject(userId.toString())
            .compact()


    fun createPasswordRecoveryToken(userId: Int): String =
        Jwts
            .builder()
            .configure(jwtConf.recoveryPasswordValidity)
            .setSubject(userId.toString())
            .claim(Claims.PASSWORD_RECOVERY.value, true)
            .compact()

    fun createJoinGameToken(userId: Int, gameType: GameType): String =
        Jwts
            .builder()
            .configure(jwtConf.joinGameValidity)
            .setSubject(userId.toString())
            .claim(Claims.JOIN_GAME.value, gameType)
            .compact()

    fun extractIdFromToken(): Int? {
        val token: String = extractTokeFromRequest(currentRequest!!) ?: return null

        try{
            return Jwts
                .parserBuilder().configure().build()
                .parseClaimsJws(token)
                .body.subject.toInt()
        }
        catch (e: SignatureException) {
            logger.warn("Token authentication failed due invalid signature")
        } catch (e: ExpiredJwtException) {
            logger.debug("Token authentication failed due to expired token")
        } catch (e: JwtException) {
            logger.debug("Token authentication failed due to exception: $e")
        }

        return null
    }

    fun extractIdFromToken(token: String?): Int? {

        if (token == null) return token

        try{
            return Jwts
                .parserBuilder().configure().build()
                .parseClaimsJws(token)
                .body.subject.toInt()
        }
        catch (e: SignatureException) {
            logger.warn("Token authentication failed due invalid signature")
        } catch (e: ExpiredJwtException) {
            logger.debug("Token authentication failed due to expired token")
        } catch (e: JwtException) {
            logger.debug("Token authentication failed due to exception: $e")
        }

        return null
    }

    fun isRecoveryPasswordToken(): Boolean {
        val token: String = extractTokeFromRequest(currentRequest!!) ?: return false

        return Jwts
            .parserBuilder().configure().build()
            .parseClaimsJws(token)
            .body.get(Claims.PASSWORD_RECOVERY.value, Boolean::class.javaObjectType) ?: false
    }

    fun getGameTypeFromToken(): GameType? {
        val token: String = extractTokeFromRequest(currentRequest!!) ?: return null

        return Jwts
            .parserBuilder().configure().build()
            .parseClaimsJws(token)
            .body.get(Claims.JOIN_GAME.value, GameType::class.javaObjectType) ?: null
    }

    private fun extractTokeFromRequest(request: HttpServletRequest): String? {
        val authorizationHeader: String = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null

        return if(authorizationHeader.startsWith("Bearer ")) authorizationHeader.substring(7) else null
    }

    private fun JwtBuilder.configure(validity: Long): JwtBuilder {
        setIssuer(jwtConf.issuer)
        setAudience(jwtConf.audience)
        setExpiration(Date(Date().time + validity))
        signWith(jwtConf.key)
        return this
    }

    private fun JwtParserBuilder.configure(): JwtParserBuilder {
        requireIssuer(jwtConf.issuer)
        requireAudience(jwtConf.audience)
        setSigningKey(jwtConf.key)
        return this
    }

}