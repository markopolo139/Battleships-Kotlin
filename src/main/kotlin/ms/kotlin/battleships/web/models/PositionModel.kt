package ms.kotlin.battleships.web.models

import ms.kotlin.battleships.business.value.Position
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Positive

class PositionModel(
    @field:Min(value = Position.MINIMAL.toLong()) @field:Max(value = Position.MAXIMUM.toLong()) val x: Int,
    @field:Min(value = Position.MINIMAL.toLong()) @field:Max(value = Position.MAXIMUM.toLong()) val y: Int,
)