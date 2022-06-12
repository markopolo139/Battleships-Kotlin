package ms.kotlin.battleships.app.security

import ms.kotlin.battleships.app.entities.ShipEntity
import ms.kotlin.battleships.app.entities.ShotEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class UserEntity(
    val id: Int,
    username: String,
    password: String,
    val email: String,
    val gameToken: String,
    val shipBoard: Set<ShipEntity>,
    val shotBoard: Set<ShotEntity>,
    authorities: Collection<GrantedAuthority>
): User(username, password, authorities)