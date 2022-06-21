package ms.kotlin.battleships.web.models.request

import ms.kotlin.battleships.web.models.PositionModel
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

class PositionShipPayload(
    @NotEmpty val positionList: List<List<@Valid PositionModel>>
)