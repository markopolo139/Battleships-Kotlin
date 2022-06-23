package ms.kotlin.battleships.app.services

import ms.kotlin.battleships.app.entities.AppShipEntity
import ms.kotlin.battleships.app.entities.AppShotEntity
import ms.kotlin.battleships.app.entities.ShipElementEntity
import ms.kotlin.battleships.app.exception.GameNotFoundException
import ms.kotlin.battleships.app.exception.NotCurrentTurnException
import ms.kotlin.battleships.app.exception.ShipsNotPlacedException
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
import ms.kotlin.battleships.business.exception.*
import ms.kotlin.battleships.business.service.GameService
import ms.kotlin.battleships.business.value.ShotType
import ms.kotlin.battleships.web.models.PositionModel
import ms.kotlin.battleships.web.models.response.ShotModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.apache.logging.log4j.LogManager
import org.springframework.transaction.annotation.Transactional

@Service
class GameInteractor {

    companion object {
        private val logger = LogManager.getLogger()
    }

    private val gameService = GameService()

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var gameRepository: GameRepository

    @Autowired
    private lateinit var simpMessagingTemplate: SimpMessagingTemplate

    private val userId
        get() = (SecurityContextHolder.getContext()?.authentication?.principal as? AppUserEntity )?.id!!

    @Transactional
    fun makeShot(positionModel: PositionModel) {
        val player = userRepository.getReferenceById(userId)
        val enemy = getEnemyEntity()
        val gameEntity = getGameEntity()

        if(player.ships.isEmpty() || enemy.ships.isEmpty())
            throw ShipsNotPlacedException()

        if(userId != gameEntity.currentPlayer.id)
            throw NotCurrentTurnException()

        try {
            val madeShot = gameService.makeShot(
                positionModel.toApp(),
                ShotBoard(player.shots.map { it.toEntity() }.toSet().toPositionMap()),
                ShipBoard(enemy.ships.map { it.toEntity() }.toSet(), gameEntity.gameType)
            )

            player.shots.add(AppShotEntity(0, madeShot.position, madeShot.shotType).toPersistence())

            userRepository.save(player)

            if (madeShot.shotType == ShotType.MISS) {
                gameEntity.currentPlayer = enemy
                gameRepository.save(gameEntity)
            }

            simpMessagingTemplate.convertAndSendToUser(
                player.username, "/queue/shot/board", ShotModel(madeShot.position.toModel(), madeShot.shotType.name)
            )
            simpMessagingTemplate.convertAndSendToUser(
                enemy.username, "/queue/ship/board", ShotModel(madeShot.position.toModel(), madeShot.shotType.name)
            )

        }
        catch (ex: InvalidShotException) {
            logger.error("$positionModel was already in database")
            throw ex
        }
        catch (ex: PositionOutOfRangeException) {
            logger.error("$positionModel is out of range")
            throw ex
        }
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
            try {
                ship.validate()
            }
            catch (ex: ShipContinuityException) {
                logger.error("$ship, is not continued")
                throw ex
            }
            catch (ex: ShipInvalidRowException) {
                logger.error("$ship, is not placed in one row")
                throw ex
            }
            catch (ex: InvalidShipException) {
                logger.error("$ship, is invalid")
                throw ex
            }
            shipList.add(ship)
        }

        val shipBoard = ShipBoard(shipList.toSet(), gameEntity.gameType )
        try {
            shipBoard.validate()
        }
        catch (ex: InvalidShipException) {
            logger.error("Ships in list overlaps")
            throw ex
        }
        catch (ex: InvalidLayoutException) {
            logger.error("Ships are not in correct layout")
            throw ex
        }

        player.ships.addAll(shipList.map { it.toPersistence() })

        userRepository.save(player)

    }

    private fun getGameEntity(): GameEntity = gameRepository.getByPlayerId(userId) ?: throw GameNotFoundException()

    private fun getEnemyEntity(): UserEntity {
        val gameEntity = getGameEntity()

        return if (gameEntity.playerA.id == userId) gameEntity.playerB else gameEntity.playerA
    }

}