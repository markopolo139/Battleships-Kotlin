package ms.kotlin.battleships.web.models.response

import ms.kotlin.battleships.web.models.PositionModel
import ms.kotlin.battleships.web.validators.ShotTypeValidator
import javax.validation.Valid

class ShotModel(
    @field:Valid val position: PositionModel,
    @field:Valid @field:ShotTypeValidator val shotType: String
)