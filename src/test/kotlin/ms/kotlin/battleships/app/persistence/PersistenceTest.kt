package ms.kotlin.battleships.app.persistence

import ms.kotlin.battleships.app.persistence.embedabble.ShipElementEmbeddable
import ms.kotlin.battleships.app.persistence.entities.GameEntity
import ms.kotlin.battleships.app.persistence.entities.ShipEntity
import ms.kotlin.battleships.app.persistence.entities.ShotEntity
import ms.kotlin.battleships.app.persistence.entities.UserEntity
import ms.kotlin.battleships.app.persistence.repositories.GameRepository
import ms.kotlin.battleships.app.persistence.repositories.ShipRepository
import ms.kotlin.battleships.app.persistence.repositories.ShotRepository
import ms.kotlin.battleships.app.persistence.repositories.UserRepository
import ms.kotlin.battleships.business.value.GameType
import ms.kotlin.battleships.business.value.ShotType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PersistenceTest {

    @Autowired
    private lateinit var gameRepository: GameRepository

    @Autowired
    private lateinit var shipRepository: ShipRepository

    @Autowired
    private lateinit var shotRepository: ShotRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `persistence entity save test`() {

        gameRepository.deleteAll()
        userRepository.deleteAll()

        val playerA = UserEntity(0, "marek", "zsk", "test", null, mutableSetOf(), mutableSetOf())
        val playerB = UserEntity(0, "kamil", "zsk", "test2", null, mutableSetOf(), mutableSetOf())

        val shotSet = mutableSetOf(
            ShotEntity(1, 1,2, ShotType.MISS),
            ShotEntity(2, 1,8, ShotType.HIT),
            ShotEntity(3, 5,2, ShotType.MISS),
            ShotEntity(4, 6,2, ShotType.HIT),
        )

        val shipSet = mutableSetOf(
            ShipEntity(1, setOf(ShipElementEmbeddable(1,2), ShipElementEmbeddable(2,2))),
            ShipEntity(2, setOf(ShipElementEmbeddable(2,3), ShipElementEmbeddable(3,3))),
            ShipEntity(3, setOf(ShipElementEmbeddable(1,1), ShipElementEmbeddable(1,2))),
        )

        Assertions.assertDoesNotThrow {
            userRepository.save(playerA)
            userRepository.save(playerB)
        }

        playerA.shots.addAll(shotSet)
        Assertions.assertDoesNotThrow {
            userRepository.save(playerA)
        }

        playerA.ships.addAll(shipSet)
        Assertions.assertDoesNotThrow {
            userRepository.save(playerA)
        }


        Assertions.assertDoesNotThrow {
            gameRepository.save(GameEntity(0, GameType.CLASSIC, playerA, playerB, playerA))
        }


    }

}