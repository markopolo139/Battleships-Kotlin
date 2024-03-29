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

        var playerA = UserEntity(0, "marko", "zsk", "test", null, mutableSetOf(), mutableSetOf(), mutableSetOf("ADMIN"))
        var playerB = UserEntity(0, "kamil", "zsk", "test2", null, mutableSetOf(), mutableSetOf(), mutableSetOf())

        val shotSet = mutableSetOf(
            ShotEntity(1, 1,2, ShotType.MISS),
            ShotEntity(2, 1,8, ShotType.HIT),
            ShotEntity(3, 5,2, ShotType.MISS),
            ShotEntity(4, 6,2, ShotType.HIT),
        )

        val shipSet = mutableSetOf(
            ShipEntity(0, setOf(ShipElementEmbeddable(1,2), ShipElementEmbeddable(2,2))),
            ShipEntity(0, setOf(ShipElementEmbeddable(2,3), ShipElementEmbeddable(3,3))),
            ShipEntity(0, setOf(ShipElementEmbeddable(1,3), ShipElementEmbeddable(1,4))),
        )

        Assertions.assertDoesNotThrow {
            playerA = userRepository.save(playerA)
            playerB = userRepository.save(playerB)
        }

        playerA.shots.addAll(shotSet)
        Assertions.assertDoesNotThrow {
            playerA = userRepository.save(playerA)
        }

        playerA.ships.addAll(shipSet)
        Assertions.assertDoesNotThrow {
            playerA = userRepository.save(playerA)
        }


        Assertions.assertDoesNotThrow {
            gameRepository.save(GameEntity(0, GameType.CLASSIC, playerA, playerB, playerA))
        }

        gameRepository.deleteAll()
        userRepository.deleteAllById(listOf(playerA.id, playerB.id))

        var playerC = UserEntity(0, "test2", "zsk", "test3", null, mutableSetOf(
            ShipEntity(0, setOf(ShipElementEmbeddable(1,2)))
        ), mutableSetOf(
            ShotEntity(0, 1, 1, ShotType.HIT)
        ), mutableSetOf("ADMIN"))

        playerC = userRepository.save(playerC)

        userRepository.deleteShipsAndShots(playerC.id)

        userRepository.deleteById(playerC.id)
    }

}