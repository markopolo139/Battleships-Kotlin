package ms.kotlin.battleships.business.value

import ms.kotlin.battleships.business.exception.PositionOutOfRangeException

data class Position(
    val x: Int,
    val y: Int
) {
    companion object {
        private const val MINIMAL = 1
        private const val MAXIMUM = 10

        fun validatePosition(position: Position) {
            if (position.x !in MINIMAL..MAXIMUM || position.y !in MINIMAL..MAXIMUM)
                throw PositionOutOfRangeException(position, MINIMAL, MAXIMUM)
        }
    }
}