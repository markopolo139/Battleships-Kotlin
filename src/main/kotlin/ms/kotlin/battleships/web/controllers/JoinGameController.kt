package ms.kotlin.battleships.web.controllers

import ms.kotlin.battleships.app.services.JoinGameService
import ms.kotlin.battleships.web.validators.GameTypeValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Validated
@CrossOrigin
@RestController
class JoinGameController {

    @Autowired
    private lateinit var joinGameService: JoinGameService

    @PostMapping("/join/game")
    fun joinGame(@RequestParam joinGameToken: String) {
        joinGameService.joinGame(joinGameToken)
    }

    @GetMapping("/join/game")
    fun createJoinGameToken(@RequestParam @Valid @GameTypeValidator gameType: String): String =
        joinGameService.createJoinGameToken(gameType)

    @PostMapping("/exit/game")
    fun exitGame() {
        joinGameService.exitGame()
    }
}