package ms.kotlin.battleships.web.controllers

import ms.kotlin.battleships.app.services.GameInteractor
import ms.kotlin.battleships.web.models.PositionModel
import ms.kotlin.battleships.web.models.request.PositionShipPayload
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Validated
@CrossOrigin
@RestController
class GameController {

    @Autowired
    private lateinit var gameInteractor: GameInteractor

    @PostMapping("/position/ships")
    fun positionShips(@RequestBody @Valid positionShipPayload: PositionShipPayload) {
        gameInteractor.placeShips(positionShipPayload.positionList)
    }

    //TODO : Test
    @MessageMapping("/make/shot")
    fun makeShot(@Valid positionModel: PositionModel) {
        gameInteractor.makeShot(positionModel)
    }

}