package ms.kotlin.battleships.business.value

import ms.kotlin.battleships.business.entity.Ship
import ms.kotlin.battleships.business.exception.InvalidLayoutException

enum class GameType {

    CLASSIC {
        override val layout: Map<Int, Int>
            get() = mapOf(
                2 to 1,
                3 to 2,
                4 to 1,
                5 to 1
            )
    },
    RUSSIAN {
        override val layout: Map<Int, Int>
            get() = mapOf(
                2 to 4,
                3 to 3,
                4 to 2,
                6 to 1
            )
    };

    abstract val layout: Map<Int, Int>

    fun validateLayout(ships: Set<Ship>) =
        if(layout != ships.groupingBy { it.shipElements.size }.eachCount()) throw InvalidLayoutException() else Unit

}