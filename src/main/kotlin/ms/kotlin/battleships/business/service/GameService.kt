package ms.kotlin.battleships.business.service

import ms.kotlin.battleships.business.entity.Ship
import ms.kotlin.battleships.business.entity.ShipBoard
import ms.kotlin.battleships.business.entity.ShotBoard
import ms.kotlin.battleships.business.exception.ShotNotFoundException
import ms.kotlin.battleships.business.value.Position
import ms.kotlin.battleships.business.value.Shot
import ms.kotlin.battleships.business.value.ShotType

class GameService {

    fun makeShot(position: Position, playerShotBoard: ShotBoard, enemyShipBoard: ShipBoard): Shot {

        val shot: Shot = if (enemyShipBoard.isShipInPosition(position)){
            Shot(position, ShotType.HIT)
        } else {
            Shot(position, ShotType.MISS)
        }

        playerShotBoard.addShot(shot)

        return shot
    }

    fun isWin(playerShotBoard: ShotBoard, enemyShipBoard: ShipBoard): Boolean =
        enemyShipBoard.ships.size == onlySankShips(playerShotBoard, enemyShipBoard).size

    private fun isShipSank(ship: Ship, playerShotBoard: ShotBoard): Boolean {
        for (shipElement in ship.shipElements) {
            try {
                playerShotBoard.getShot(shipElement.position)
            }
            catch (ex: ShotNotFoundException) {
                return false
            }
        }

        return true
    }

    fun onlyNotSankShips(enemyShotBoard: ShotBoard, playerShipBoard: ShipBoard): List<Ship> =
        playerShipBoard.ships.asSequence().filter { !isShipSank(it, enemyShotBoard) }.toList()

    fun onlySankShips(playerShotBoard: ShotBoard, enemyShipBoard: ShipBoard): List<Ship> =
        enemyShipBoard.ships.asSequence().filter { isShipSank(it, playerShotBoard) }.toList()

}