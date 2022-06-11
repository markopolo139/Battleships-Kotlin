package ms.kotlin.battleships.app.entities

import ms.kotlin.battleships.business.value.Position
import ms.kotlin.battleships.business.value.Shot
import ms.kotlin.battleships.business.value.ShotType

class ShotEntity(
    val id: Int,
    position: Position,
    shotType: ShotType
): Shot(position, shotType) {
}