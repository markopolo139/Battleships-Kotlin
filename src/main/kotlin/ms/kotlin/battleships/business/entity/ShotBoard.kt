package ms.kotlin.battleships.business.entity

import ms.kotlin.battleships.business.exception.InvalidShotException
import ms.kotlin.battleships.business.exception.ShotNotFoundException
import ms.kotlin.battleships.business.value.Position
import ms.kotlin.battleships.business.value.Shot

class ShotBoard(
    private val shotList: MutableMap<Position, Shot>
) {

    private fun validateShot(shot: Shot) {
        Position.validatePosition(shot.position)
        if (shotList[shot.position] != null)
            throw InvalidShotException("Selected spot was already made")
    }

    fun addShot(shot: Shot) {
        validateShot(shot)
        shotList[shot.position] = shot
    }

    fun getShot(position: Position) = shotList[position] ?: throw ShotNotFoundException()

}