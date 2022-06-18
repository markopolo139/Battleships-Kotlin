package ms.kotlin.battleships.web.models.response

import ms.kotlin.battleships.web.models.PositionModel
import ms.kotlin.battleships.web.validators.GameTypeValidator
import javax.validation.Valid

class ShotModel(
    @field:Valid val position: PositionModel,
    @field:Valid @field:GameTypeValidator val gameType: String
)