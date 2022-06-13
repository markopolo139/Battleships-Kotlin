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
    authorities: Collection<GrantedAuthority>
): User(username, password, authorities) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as AppUserEntity

        if (id != other.id) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id
        result = 31 * result + email.hashCode()
        return result
    }
}