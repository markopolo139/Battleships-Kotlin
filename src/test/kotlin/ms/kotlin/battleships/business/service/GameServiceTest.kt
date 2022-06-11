package ms.kotlin.battleships.business.service

import ms.kotlin.battleships.business.entity.Ship
import ms.kotlin.battleships.business.entity.ShipBoard
import ms.kotlin.battleships.business.entity.ShotBoard
import ms.kotlin.battleships.business.value.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GameServiceTest {

    private val gameService = GameService()

    private val shotBoard = ShotBoard(
        mutableMapOf(
            Position(1,1) to Shot(Position(1,1), ShotType.HIT),
            Position(2,1) to Shot(Position(2,1), ShotType.HIT),
            Position(3,1) to Shot(Position(3,1), ShotType.HIT),
            Position(2,2) to Shot(Position(2,2), ShotType.MISS),
        )
    )

    private val notWinBoard = ShipBoard(
        setOf(
            Ship(
                setOf(
                    ShipElement(Position(1,1)),
                    ShipElement(Position(2,1)),
                    ShipElement(Position(3,1))
                )
            ),
            Ship(
                setOf(
                    ShipElement(Position(3,2)),
                    ShipElement(Position(4,2)),
                    ShipElement(Position(5,2))
                )
            )
        ),
        GameType.CLASSIC
    )

    @Test
    fun `test is ship sank function`() {

        Assertions.assertEquals(
            gameService.onlyNotSankShips(shotBoard, notWinBoard)[0], Ship(
                setOf(
                    ShipElement(Position(3,2)),
                    ShipElement(Position(4,2)),
                    ShipElement(Position(5,2))
                )
            )
        )

        Assertions.assertEquals(
            gameService.onlySankShips(shotBoard, notWinBoard)[0], Ship(
                setOf(
                    ShipElement(Position(1,1)),
                    ShipElement(Position(2,1)),
                    ShipElement(Position(3,1))
                )
            )
        )

    }

    @Test
    fun `test make shot function`() {

        Assertions.assertEquals(
            gameService.makeShot(Position(3,2), shotBoard, notWinBoard),
            Shot(Position(3,2), ShotType.HIT)
        )

        Assertions.assertEquals(
            gameService.makeShot(Position(8,8), shotBoard, notWinBoard),
            Shot(Position(8,8), ShotType.MISS)
        )

    }

    @Test
    fun `test is win function`() {

        val winBoard = ShipBoard(
            setOf(
                Ship(
                    setOf(
                        ShipElement(Position(1,1)),
                        ShipElement(Position(2,1)),
                        ShipElement(Position(3,1))
                    )
                )
            ),
            GameType.CLASSIC
        )

        Assertions.assertTrue(gameService.isWin(shotBoard, winBoard))

        Assertions.assertFalse(gameService.isWin(shotBoard, notWinBoard))

    }

}