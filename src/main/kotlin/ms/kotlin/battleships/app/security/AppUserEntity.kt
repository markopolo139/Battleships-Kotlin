package ms.kotlin.battleships.app.security

import ms.kotlin.battleships.app.entities.AppShipEntity
import ms.kotlin.battleships.app.entities.AppShotEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class AppUserEntity(
    val id: Int,
    username: String,
    password: String,
    val email: String,
    val gameToken: String,
    val shipBoard: Set<AppShipEntity>,
    val shotBoard: Set<AppShotEntity>,
    authorities: Collection<GrantedAuthority>
): User(username, password, authorities)