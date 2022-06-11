package ms.kotlin.battleships.business.entity

import ms.kotlin.battleships.business.exception.InvalidShipException
import ms.kotlin.battleships.business.value.GameType
import ms.kotlin.battleships.business.value.Position
import kotlin.math.abs

class ShipBoard(
    val ships: Set<Ship>,
    private val gameType: GameType
) {

    fun validate() {
        gameType.validateLayout(ships)
        val validatedList = mutableListOf<Ship>()

        for (s1 in ships) {
            validatedList.add(s1)
            for (s2 in ships) {
                if (validatedList.contains(s2)) continue
                if (!validateShipPosition(s1, s2))
                    throw InvalidShipException("Ship overlaps")
            }
        }
    }

    private fun validateShipPosition(ship1: Ship, ship2: Ship): Boolean {
        for (shipElement1 in ship1.shipElements) {
            for (shipElement2 in ship2.shipElements) {
                if (validateRelativePosition(shipElement1.position, shipElement2.position))
                    return false
            }
        }

        return true
    }

    private fun validateRelativePosition(position1: Position, position2: Position) = position1 == position2

    fun isShipInPosition(position: Position): Boolean =
        ships.flatMap { it.shipElements }.asSequence().map { it.position }.any { it == position }

}