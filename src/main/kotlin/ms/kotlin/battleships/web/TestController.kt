package ms.kotlin.battleships.web

import ms.kotlin.battleships.app.services.AuthService
import org.springframework.beans.factory.annotation.Autowired
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

}