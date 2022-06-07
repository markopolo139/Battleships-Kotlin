package ms.kotlin.battleships.business.entity

import ms.kotlin.battleships.business.exception.InvalidShotException
import ms.kotlin.battleships.business.exception.PositionOutOfRangeException
import ms.kotlin.battleships.business.value.Position
import ms.kotlin.battleships.business.value.Shot
import ms.kotlin.battleships.business.value.ShotType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ShotBoardTest {

    @Test
    fun `test shot board validation`() {

        val shotBoard = ShotBoard(mutableMapOf())

        Assertions.assertThrows(PositionOutOfRangeException::class.java) {
            shotBoard.addShot(Shot(Position(-1,11), ShotType.HIT))
        }

        shotBoard.addShot(Shot(Position(1,1), ShotType.HIT))

        Assertions.assertThrows(InvalidShotException::class.java) {
            shotBoard.addShot(Shot(Position(1,1), ShotType.HIT))
        }

    }

}