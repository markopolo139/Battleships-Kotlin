package ms.kotlin.battleships.app.security

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.SecretKey
import kotlin.math.log

@Component
class JwtConf {

    companion object {
        private val logger = LogManager.getLogger()

        const val DEFAULT_VALIDITY: Long = 900000
        const val DEFAULT_PASSWORD_RECOVERY_VALIDITY: Long = 180000
        const val DEFAULT_JOIN_GAME_VALIDITY: Long = 540000
    }

    @Value("\${api.auth.token.secret}")
    private lateinit var secret: String

    private var _key: SecretKey? = null

    val key: SecretKey
        get() {
            if (_key == null) initKey()
            return _key!!
        }

    @Value("\${api.auth.token.issuer}")
    lateinit var issuer: String

    @Value("\${api.auth.token.audience}")
    lateinit var audience: String

    @Value("\${api.auth.token.validity}")
    private lateinit var validityValue: String

    private var _validity: Long? = null

    val validity: Long
        get() {
            if (_validity == null) initValidity()

            return _validity!!
        }

    @Value("\${api.auth.token.password.recovery.validity}")
    private lateinit var recoveryPasswordValidityValue: String

    private var _recoveryPasswordValidity: Long? = null

    val recoveryPasswordValidity: Long
        get() {
            if (_recoveryPasswordValidity == null) initPasswordRecoveryValidity()

            return _recoveryPasswordValidity!!
        }


    @Value("\${api.auth.token.join.game.validity}")
    private lateinit var joinGameValidityValue: String

    private var _joinGameValidity: Long? = null

    val joinGameValidity: Long
        get() {
            if (_joinGameValidity == null) initJoinGameValidity()

            return _joinGameValidity!!
        }

    private fun initKey() =
        if (secret.isBlank()) _key = Keys.secretKeyFor(SignatureAlgorithm.HS512)
        else _key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    private fun initValidity() {
        if (validityValue.isNotBlank()) try {
            _validity = validityValue.toLong()
            return
        }
        catch (ex: Exception) {
            logger.warn("No value for validity found, using default value which is $DEFAULT_VALIDITY")
        }

        _validity = DEFAULT_VALIDITY
    }

    private fun initPasswordRecoveryValidity() {
        if (recoveryPasswordValidityValue.isNotBlank()) try {
            _recoveryPasswordValidity = recoveryPasswordValidityValue.toLong()
            return
        } catch (ex: Exception) {
            logger.warn("No value for password recovery validity found, using default value which is $DEFAULT_PASSWORD_RECOVERY_VALIDITY")
        }

        _recoveryPasswordValidity = DEFAULT_PASSWORD_RECOVERY_VALIDITY
    }

    private fun initJoinGameValidity() {
        if (joinGameValidityValue.isNotBlank()) try {
            _joinGameValidity = joinGameValidityValue.toLong()
            return
        } catch (ex: Exception) {
            logger.warn("No value for join game validity found, using default value which is $DEFAULT_JOIN_GAME_VALIDITY")
        }

        _joinGameValidity = DEFAULT_JOIN_GAME_VALIDITY
    }
}