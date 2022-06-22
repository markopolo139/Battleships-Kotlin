package ms.kotlin.battleships.web.controllers

import ms.kotlin.battleships.app.services.GameStatusService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@Validated
@RestController
class GameStatusController {

    @Autowired
    private lateinit var gameStatusService: GameStatusService

    @GetMapping("/ships")
    fun getShips() = gameStatusService.getShipBoard()

    @GetMapping("/shots")
    fun getShots() = gameStatusService.getShotBoard()

    @GetMapping("/enemy/sank/ships")
    fun getSankShips() = gameStatusService.getEnemySankShips()

    @GetMapping("/enemy/shots")
    fun getEnemyShots() = gameStatusService.getEnemyShots()

    @GetMapping("/is/win")
    fun isWin() = gameStatusService.isWin()

    @GetMapping("/current/game/type")
    fun getGameType() = gameStatusService.getGameType()

    @GetMapping("/current/player/turn")
    fun getCurrentPlayerId() = gameStatusService.getCurrentTurn()

}