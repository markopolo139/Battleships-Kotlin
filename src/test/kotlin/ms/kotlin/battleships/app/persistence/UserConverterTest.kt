package ms.kotlin.battleships.app.persistence

import ms.kotlin.battleships.app.entities.AppShipEntity
import ms.kotlin.battleships.app.entities.AppShotEntity
import ms.kotlin.battleships.app.entities.ShipElementEntity
import ms.kotlin.battleships.app.persistence.embedabble.ShipElementEmbeddable
import ms.kotlin.battleships.app.persistence.entities.ShipEntity
import ms.kotlin.battleships.app.persistence.entities.ShotEntity
import ms.kotlin.battleships.app.persistence.entities.UserEntity
import ms.kotlin.battleships.app.security.AppUserEntity
import ms.kotlin.battleships.business.value.Position
import ms.kotlin.battleships.business.value.ShotType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

class UserConverterTest {

    @Test
    fun `test conversion between userEntity and persistence user`() {

        val userPersistence = UserEntity(
            1, "Marek", "ZSK", "marek@onet.pl", null,
            mutableSetOf(ShipEntity(1, setOf(ShipElementEmbeddable(1,2),ShipElementEmbeddable(1,1)))),
            mutableSetOf(ShotEntity(1,1,1, ShotType.HIT), ShotEntity(2,1,3,ShotType.MISS)),
            mutableSetOf("ADMIN", "USER")
        )
        val userApp = AppUserEntity(
            1, "Marek", "ZSK", "marek@onet.pl", "",
            mutableSetOf(AppShipEntity(1, setOf(ShipElementEntity(Position(1,2)),ShipElementEntity(Position(1,1))))),
            mutableSetOf(AppShotEntity(1, Position(1,1), ShotType.HIT), AppShotEntity(2,Position(1,3),ShotType.MISS)),
            setOf(ImplementGrantedAuthority("ROLE_ADMIN"), ImplementGrantedAuthority("ROLE_USER"))
        )

        Assertions.assertEquals(userPersistence, userApp.toPersistence())
        Assertions.assertEquals(userApp, userPersistence.toEntity())

    }

}

class ImplementGrantedAuthority(private val role: String): GrantedAuthority {
    override fun getAuthority(): String = role
}