package ms.kotlin.battleships.web.models.response

import ms.kotlin.battleships.web.models.PositionModel
import org.springframework.validation.annotation.Validated
import javax.validation.Valid

class ShipElementModel(
    @field:Valid val position: PositionModel
)
