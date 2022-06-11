package ms.kotlin.battleships.app.persistence

import ms.kotlin.battleships.app.entities.ShipElementEntity
import ms.kotlin.battleships.app.persistence.embedabble.ShipElementEmbeddable
import ms.kotlin.battleships.app.persistence.entities.GameEntity
import ms.kotlin.battleships.app.persistence.entities.ShipEntity
import ms.kotlin.battleships.app.persistence.entities.ShotEntity
import ms.kotlin.battleships.app.persistence.entities.UserEntity
import ms.kotlin.battleships.business.value.Position

typealias AppUserEntity = ms.kotlin.battleships.app.entities.UserEntity
typealias AppShipEntity = ms.kotlin.battleships.app.entities.ShipEntity
typealias AppShotEntity = ms.kotlin.battleships.app.entities.ShotEntity
typealias AppGameEntity = ms.kotlin.battleships.app.entities.GameEntity

fun ShipElementEmbeddable.toEntity() = ShipElementEntity(Position(x, y))
fun ShipElementEntity.toPersistence() = ShipElementEmbeddable(position.x, position.y)

fun ShipEntity.toEntity() = AppShipEntity(id, shipElements.map { it.toEntity() }.toSet())
fun AppShipEntity.toPersistence() = ShipEntity(id, shipElements.map { it as ShipElementEmbeddable }.toSet())

fun ShotEntity.toEntity() = AppShotEntity(id, Position(x, y), shotType)
fun AppShotEntity.toPersistence() = ShotEntity(id, position.x, position.y, shotType)

fun UserEntity.toEntity() = AppUserEntity(
    id, username, password, email, gameToken ?: "",
    ships.map { it.toEntity() }.toSet(), shots.map { it.toEntity() }.toSet()
)

fun AppUserEntity.toPersistence() = UserEntity(
    id, username, password, email, gameToken.ifBlank { null },
    shipBoard.map { it.toPersistence() }.toMutableSet(), shotBoard.map { it.toPersistence() }.toMutableSet()
)

fun GameEntity.toEntity() =
    AppGameEntity(id, gameType, playerA.toEntity(), playerB.toEntity(), currentPlayer.toEntity())
fun AppGameEntity.toPersistence() =
    GameEntity(id, gameType, playerA.toPersistence(), playerB.toPersistence(), currentPlayer.toPersistence())