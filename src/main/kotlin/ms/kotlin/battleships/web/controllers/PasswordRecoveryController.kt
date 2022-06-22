package ms.kotlin.battleships.web.controllers

import ms.kotlin.battleships.app.recovery.ForgotPasswordService
import ms.kotlin.battleships.web.utils.serverPath
import ms.kotlin.battleships.web.validators.PasswordValidatorAnnotation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.Email

@Validated
@CrossOrigin
@RestController
class PasswordRecoveryController {

    @Autowired
    private lateinit var forgotPasswordService: ForgotPasswordService

    @PostMapping("/recoverpwd")
    fun sendEmail(@RequestParam @Valid @Email email: String, httpServletRequest: HttpServletRequest) {
        forgotPasswordService.sendEmail(email, httpServletRequest.serverPath)
    }

    @PostMapping("/changepwd")
    fun changePassword(@RequestParam @Valid @PasswordValidatorAnnotation password: String) =
        forgotPasswordService.changePassword(password)

}