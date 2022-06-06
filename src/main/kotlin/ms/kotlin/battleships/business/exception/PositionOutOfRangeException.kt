package ms.kotlin.battleships.business.exception

import ms.kotlin.battleships.business.value.Position

class PositionOutOfRangeException(position: Position, min: Int, max: Int): BusinessException(
    "Selected position (x = ${position.x}, y = ${position.y}) is out of range(min = $min, max = $max)"
)