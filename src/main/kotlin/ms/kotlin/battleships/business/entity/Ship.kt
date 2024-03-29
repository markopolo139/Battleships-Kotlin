package ms.kotlin.battleships.business.entity

import ms.kotlin.battleships.business.exception.InvalidShipException
import ms.kotlin.battleships.business.exception.ShipContinuityException
import ms.kotlin.battleships.business.exception.ShipInvalidRowException
import ms.kotlin.battleships.business.value.Position
import ms.kotlin.battleships.business.value.ShipElement

open class Ship(
    val shipElements: Set<ShipElement>
) {

    fun validate() {
        if (shipElements.isEmpty())
            throw InvalidShipException("Ship does not have any ship elements")

        shipElements.asSequence().map { it.position }.forEach { Position.validatePosition(it) }

        if (!isListDistinct())
            throw InvalidShipException("Ship does not have unique positions")

        if(validateRow(shipElements.map { it.position }.map { it.x }.toList())) {
            if (validateContinuity(shipElements.map { it.position }.map { it.y }.toList()))
                return

            throw ShipContinuityException()
        }

        if(validateRow(shipElements.map { it.position }.map { it.y }.toList())) {
            if (validateContinuity(shipElements.map { it.position }.map { it.x }.toList()))
                return

            throw ShipContinuityException()
        }

        throw ShipInvalidRowException()
    }

    private fun validateContinuity(modifiedList: List<Int>): Boolean {

        val min = modifiedList.minOf { it }
        val max = modifiedList.maxOf { it }

        if (max - min + 1 != modifiedList.size)
            return false

        for (element in modifiedList)
            if (element !in min..max)
                return false

        return true
    }

    private fun isListDistinct(): Boolean =
        shipElements.asSequence().map { it.position }.distinct().count() == shipElements.size

    private fun validateRow(modifiedList: List<Int>): Boolean = modifiedList.distinct().count() == 1
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ship

        if (shipElements != other.shipElements) return false

        return true
    }

    override fun hashCode(): Int {
        return shipElements.hashCode()
    }


}