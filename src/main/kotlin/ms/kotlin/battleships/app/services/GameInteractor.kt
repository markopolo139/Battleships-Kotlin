package ms.kotlin.battleships.app.services

import ms.kotlin.battleships.app.entities.AppShipEntity
import ms.kotlin.battleships.app.entities.ShipElementEntity
import ms.kotlin.battleships.app.persistence.entities.GameEntity
import ms.kotlin.battleships.app.persistence.entities.ShipEntity
import ms.kotlin.battleships.app.persistence.entities.UserEntity
import ms.kotlin.battleships.app.persistence.repositories.GameRepository
import ms.kotlin.battleships.app.persistence.repositories.UserRepository
import ms.kotlin.battleships.app.persistence.toEntity
import ms.kotlin.battleships.app.persistence.toPersistence
import ms.kotlin.battleships.app.security.AppUserEntity
import ms.kotlin.battleships.app.toApp
import ms.kotlin.battleships.app.toModel
import ms.kotlin.battleships.app.utils.toPositionMap
import ms.kotlin.battleships.business.entity.ShipBoard
import ms.kotlin.battleships.business.entity.ShotBoard
import ms.kotlin.battleships.business.service.GameService
import ms.kotlin.battleships.web.models.PositionModel
import ms.kotlin.battleships.web.models.response.ShotModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class GameInteractor {

    private val gameService = GameService()

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var gameRepository: GameRepository

    @Autowired
    private lateinit var simpMessagingTemplate: SimpMessagingTemplate

    private val userId
        get() = (SecurityContextHolder.getContext()?.authentication?.principal as? AppUserEntity )?.id!!

    fun makeShot(positionModel: PositionModel) {
        val player = userRepository.getReferenceById(userId)
        val enemy = getEnemyEntity()
        val gameEntity = getGameEntity()

        val madeShot = gameService.makeShot(
            positionModel.toApp(),
            ShotBoard(player.shots.map { it.toEntity() }.toSet().toPositionMap()),
            ShipBoard(enemy.ships.map { it.toEntity() }.toSet(), gameEntity.gameType)
        )

        userRepository.save(player)

        simpMessagingTemplate.convertAndSendToUser(
            player.username, "/queue/shot/board", ShotModel(madeShot.position.toModel(), madeShot.shotType.name)
        )
        simpMessagingTemplate.convertAndSendToUser(
            enemy.username, "/queue/ship/board", ShotModel(madeShot.position.toModel(), madeShot.shotType.name)
        )

    }

    fun placeShips(positions: List<List<PositionModel>>) {

        val gameEntity = getGameEntity()
        val player = userRepository.getReferenceById(userId)
        val shipList = mutableListOf<AppShipEntity>()

        for (shipsPosition in positions) {
            val shipElements = mutableSetOf<ShipElementEntity>()
            for (position in shipsPosition)
                shipElements.add(ShipElementEntity(position.toApp()))

            val ship = AppShipEntity(0, shipElements)
            ship.validate()
            shipList.add(ship)
        }

        val shipBoard = ShipBoard(shipList.toSet(), gameEntity.gameType )
        shipBoard.validate()

        player.ships.addAll(shipList.map { it.toPersistence() })

        userRepository.save(player)

    }

    private fun getGameEntity(): GameEntity = gameRepository.getByPlayerId(userId)

    private fun getEnemyEntity(): UserEntity {
        val gameEntity = getGameEntity()

        return if (gameEntity.playerA.id == userId) gameEntity.playerB else gameEntity.playerA
    }

}