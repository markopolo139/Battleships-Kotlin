package ms.kotlin.battleships.web

import ms.kotlin.battleships.app.security.AppUserEntity
import ms.kotlin.battleships.app.services.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@CrossOrigin
class TestController {

    @Autowired
    private lateinit var authService: AuthService

    @GetMapping("/auth")
    fun auth(
        @RequestParam username: String,
        @RequestParam password: String
    ): String {
        return authService.authenticate(username, password)
    }

    @GetMapping("/api/v1/test")
    fun testToken(): String {
        return "Ok"
    }

    @MessageMapping("/make/shot")
    @SendTo(value = ["/topic/ships"])
    fun testWS(): Int {
        return (SecurityContextHolder.getContext().authentication.principal as? AppUserEntity)?.id ?: 0
    }

}