package ms.kotlin.battleships.business.entity

import ms.kotlin.battleships.business.exception.InvalidShipException
import ms.kotlin.battleships.business.value.GameType
import ms.kotlin.battleships.business.value.Position
import ms.kotlin.battleships.business.value.ShipElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ShipBoardTest {

    @Test
    fun `test validation of ship board`() {

        val classicShips = listOf(
            Ship(
                listOf(
                    ShipElement(Position(1,1)),
                    ShipElement(Position(1,2)),
                )
            ),
            Ship(
                listOf(
                    ShipElement(Position(1,3)),
                    ShipElement(Position(1,4)),
                    ShipElement(Position(1,5)),
                )
            ),
            Ship(
                listOf(
                    ShipElement(Position(4,1)),
                    ShipElement(Position(4,2)),
                    ShipElement(Position(4,3)),
                )
            ),
            Ship(
                listOf(
                    ShipElement(Position(7,1)),
                    ShipElement(Position(8,1)),
                    ShipElement(Position(9,1)),
                    ShipElement(Position(10,1)),
                )
            ),
            Ship(
                listOf(
                    ShipElement(Position(10,6)),
                    ShipElement(Position(10,7)),
                    ShipElement(Position(10,8)),
                    ShipElement(Position(10,9)),
                    ShipElement(Position(10,10)),
                )
            ),
        )

        val validShipBoard = ShipBoard(classicShips, GameType.CLASSIC)

        Assertions.assertDoesNotThrow {
            validShipBoard.validate()
        }

        val invalidShips = listOf(
            Ship(
                listOf(
                    ShipElement(Position(1,3)),
                    ShipElement(Position(2,3)),
                )
            ),
            Ship(
                listOf(
                    ShipElement(Position(1,3)),
                    ShipElement(Position(1,4)),
                    ShipElement(Position(1,5)),
                )
            ),
            Ship(
                listOf(
                    ShipElement(Position(4,1)),
                    ShipElement(Position(4,2)),
                    ShipElement(Position(4,3)),
                )
            ),
            Ship(
                listOf(
                    ShipElement(Position(7,1)),
                    ShipElement(Position(8,1)),
                    ShipElement(Position(9,1)),
                    ShipElement(Position(10,1)),
                )
            ),
            Ship(
                listOf(
                    ShipElement(Position(10,6)),
                    ShipElement(Position(10,7)),
                    ShipElement(Position(10,8)),
                    ShipElement(Position(10,9)),
                    ShipElement(Position(10,10)),
                )
            ),
        )

        val invalidShipBoard = ShipBoard(invalidShips, GameType.CLASSIC)

        Assertions.assertThrows(InvalidShipException::class.java) {
            invalidShipBoard.validate()
        }

    }

}