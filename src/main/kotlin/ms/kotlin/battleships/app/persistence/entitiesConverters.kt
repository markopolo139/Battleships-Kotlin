package ms.kotlin.battleships.app.persistence

import ms.kotlin.battleships.app.entities.AppGameEntity
import ms.kotlin.battleships.app.entities.AppShipEntity
import ms.kotlin.battleships.app.entities.AppShotEntity
import ms.kotlin.battleships.app.entities.ShipElementEntity
import ms.kotlin.battleships.app.persistence.embedabble.ShipElementEmbeddable
import ms.kotlin.battleships.app.persistence.entities.GameEntity
import ms.kotlin.battleships.app.persistence.entities.ShipEntity
import ms.kotlin.battleships.app.persistence.entities.ShotEntity
import ms.kotlin.battleships.app.persistence.entities.UserEntity
import ms.kotlin.battleships.app.security.AppUserEntity
import ms.kotlin.battleships.business.value.Position
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

fun ShipElementEmbeddable.toEntity() = ShipElementEntity(Position(x, y))
fun ShipElementEntity.toPersistence() = ShipElementEmbeddable(position.x, position.y)

fun ShipEntity.toEntity() = AppShipEntity(id, shipElements.map { it.toEntity() }.toSet())
fun AppShipEntity.toPersistence() = ShipEntity(id, shipElements.map { (it as ShipElementEntity).toPersistence() }.toSet())

fun ShotEntity.toEntity() = AppShotEntity(id, Position(x, y), shotType)
fun AppShotEntity.toPersistence() = ShotEntity(id, position.x, position.y, shotType)

fun UserDetails.toAppUser(id: Int, email: String) =
    AppUserEntity(
        id, username, password, email, authorities
)

fun UserEntity.toEntity() = User
    .builder()
    .username(username)
    .password(password)
    .roles(*roles.toTypedArray())
    .build().toAppUser(id, email)

fun GameEntity.toEntity() =
    AppGameEntity(id, gameType, playerA.toEntity(), playerB.toEntity(), currentPlayer.toEntity())
