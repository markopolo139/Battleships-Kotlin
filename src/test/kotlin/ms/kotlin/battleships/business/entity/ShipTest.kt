package ms.kotlin.battleships.business.entity

import ms.kotlin.battleships.business.exception.InvalidShipException
import ms.kotlin.battleships.business.exception.ShipContinuityException
import ms.kotlin.battleships.business.exception.ShipInvalidRowException
import ms.kotlin.battleships.business.value.Position
import ms.kotlin.battleships.business.value.ShipElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

class ShipTest {

    @Test
    fun `test ship validation`() {

        val emptyListShip = Ship(emptySet())

        Assertions.assertEquals(
            "Ship does not have any ship elements",
            assertThrows<InvalidShipException> { emptyListShip.validate() }.message
        )

        val notInOneRowShip = Ship(setOf(
            ShipElement(Position(1,1)),
            ShipElement(Position(2,2)),
            ShipElement(Position(1,2)),
        ))

        Assertions.assertThrows(ShipInvalidRowException::class.java) {
            notInOneRowShip.validate()
        }

        val invalidXRowShip = Ship(setOf(
            ShipElement(Position(1,1)),
            ShipElement(Position(1,2)),
            ShipElement(Position(1,3)),
            ShipElement(Position(1,4)),
            ShipElement(Position(1,6)),
        ))

        Assertions.assertThrows(ShipContinuityException::class.java) {
            invalidXRowShip.validate()
        }

        val invalidYRowShip = Ship(setOf(
            ShipElement(Position(1,1)),
            ShipElement(Position(2,1)),
            ShipElement(Position(3,1)),
            ShipElement(Position(5,1)),
            ShipElement(Position(6,1)),
        ))

        Assertions.assertThrows(ShipContinuityException::class.java) {
            invalidYRowShip.validate()
        }

    }

}