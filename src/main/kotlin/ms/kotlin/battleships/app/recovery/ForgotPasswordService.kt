package ms.kotlin.battleships.app.recovery

import ms.kotlin.battleships.app.exception.EmailNotFoundException
import ms.kotlin.battleships.app.exception.InvalidTokenPasswordRecoveryException
import ms.kotlin.battleships.app.exception.MailCreationException
import ms.kotlin.battleships.app.persistence.repositories.UserRepository
import ms.kotlin.battleships.app.security.TokenService
import org.apache.juli.logging.LogFactory
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.io.File
import javax.mail.internet.MimeMessage

@Service
class ForgotPasswordService {

    companion object {
        private val logger = LogManager.getLogger()
        private const val  EMAIL_FROM = "springtest1@onet.pl"
        private const val  EMAIL_PATH = "/api/v1/change/password?token="
    }

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var javaMailSender: JavaMailSender

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var templateEngine: TemplateEngine

    private val userId = tokenService.extractIdFromToken()

    fun sendEmail(email: String, serverPath: String) {

        val currentUser = userRepository.getByEmail(email)

        if (currentUser == null) {
            logger.debug("Typed email $email is not saved in database")
            throw EmailNotFoundException(email)
        }

        prepareEmail(email, tokenService.createPasswordRecoveryToken(currentUser.id), serverPath)

    }

    private fun prepareEmail(email: String, token: String, serverPath: String) {

        val context = Context().apply {
           setVariable("sendPath", "$serverPath$EMAIL_PATH$token")
        }
        try{
            val mimeMessageHelper = MimeMessageHelper(javaMailSender.createMimeMessage(), true)
            mimeMessageHelper.setFrom(EMAIL_FROM)
            mimeMessageHelper.setSubject("Recovery password email")
            mimeMessageHelper.setTo(email)

            mimeMessageHelper.addInline("image", File("src/main/kotlin/resources/static/forgotPasswordImage.png"))
            mimeMessageHelper.setText(
                templateEngine.process("email", context), true
            )

            javaMailSender.send(mimeMessageHelper.mimeMessage)
        }
        catch (ex: MailException) {
            logger.info("Email could not be sent, because error occurred")
            throw MailCreationException()
        }



    }

    fun changePassword(newPassword: String) {

        if (!tokenService.isRecoveryPasswordToken()) {
            logger.debug("Used token is not for password recovery")
            throw InvalidTokenPasswordRecoveryException()
        }

        val user = userRepository.getReferenceById(userId ?: throw InvalidTokenPasswordRecoveryException()).apply {
            password = passwordEncoder.encode(newPassword)
        }

        userRepository.save(user)
    }

}