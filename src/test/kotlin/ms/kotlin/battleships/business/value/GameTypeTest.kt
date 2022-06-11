package ms.kotlin.battleships.business.value

import ms.kotlin.battleships.business.entity.Ship
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

class GameTypeTest {

    @Test
    fun `test validation of ships layout`() {

        val classicShips = setOf(
            Ship(
                setOf(
                    ShipElement(Position(1,1)),
                    ShipElement(Position(1,2)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(1,3)),
                    ShipElement(Position(1,4)),
                    ShipElement(Position(1,5)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(4,1)),
                    ShipElement(Position(4,2)),
                    ShipElement(Position(4,3)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(7,1)),
                    ShipElement(Position(8,1)),
                    ShipElement(Position(9,1)),
                    ShipElement(Position(10,1)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(10,6)),
                    ShipElement(Position(10,7)),
                    ShipElement(Position(10,8)),
                    ShipElement(Position(10,9)),
                    ShipElement(Position(10,10)),
                )
            ),
        )

        Assertions.assertTrue(GameType.CLASSIC.validateLayout(classicShips))
        Assertions.assertFalse(GameType.RUSSIAN.validateLayout(classicShips))

        val russianShips = setOf(
            Ship(
                setOf(
                    ShipElement(Position(1,1)),
                    ShipElement(Position(1,2)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(6,6)),
                    ShipElement(Position(6,7)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(1,8)),
                    ShipElement(Position(2,8)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(10,3)),
                    ShipElement(Position(10,4)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(4,1)),
                    ShipElement(Position(4,2)),
                    ShipElement(Position(4,3)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(6,3)),
                    ShipElement(Position(7,3)),
                    ShipElement(Position(8,3)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(10,6)),
                    ShipElement(Position(10,7)),
                    ShipElement(Position(10,8)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(7,1)),
                    ShipElement(Position(8,1)),
                    ShipElement(Position(9,1)),
                    ShipElement(Position(10,1)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(1,3)),
                    ShipElement(Position(1,4)),
                    ShipElement(Position(1,5)),
                    ShipElement(Position(1,6)),
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(10,5)),
                    ShipElement(Position(10,6)),
                    ShipElement(Position(10,7)),
                    ShipElement(Position(10,8)),
                    ShipElement(Position(10,9)),
                    ShipElement(Position(10,10)),
                )
            ),
        )

        Assertions.assertTrue(GameType.RUSSIAN.validateLayout(russianShips))
        Assertions.assertFalse(GameType.CLASSIC.validateLayout(russianShips))

    }

}