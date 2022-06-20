package ms.kotlin.battleships.app.services

import ms.kotlin.battleships.app.entities.AppGameEntity
import ms.kotlin.battleships.app.entities.AppShipEntity
import ms.kotlin.battleships.app.exception.GameNotFoundException
import ms.kotlin.battleships.app.exception.InvalidAuthenticationException
import ms.kotlin.battleships.app.persistence.entities.GameEntity
import ms.kotlin.battleships.app.persistence.entities.UserEntity
import ms.kotlin.battleships.app.persistence.repositories.GameRepository
import ms.kotlin.battleships.app.persistence.repositories.UserRepository
import ms.kotlin.battleships.app.persistence.toEntity
import ms.kotlin.battleships.app.security.AppUserEntity
import ms.kotlin.battleships.app.toModel
import ms.kotlin.battleships.app.utils.toPositionMap
import ms.kotlin.battleships.business.entity.ShipBoard
import ms.kotlin.battleships.business.entity.ShotBoard
import ms.kotlin.battleships.business.service.GameService
import ms.kotlin.battleships.web.models.response.ShipModel
import ms.kotlin.battleships.web.models.response.ShotModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class GameStatusService {

    @Autowired
    private lateinit var gameRepository: GameRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private val gameService = GameService()

    private val userId
        get() = (SecurityContextHolder.getContext()?.authentication?.principal as? AppUserEntity)?.id!!

    fun getShotBoard(): List<ShotModel> = userRepository.getReferenceById(userId).shots.map {it.toEntity().toModel() }

    fun getShipBoard(): List<ShipModel> = userRepository.getReferenceById(userId).ships.map { it.toEntity().toModel() }

    fun getEnemyShots(): List<ShotModel> = getEnemyEntity().shots.map { it.toEntity().toModel() }

    fun getCurrentTurn(): Int = getGameEntity().currentPlayer.id

    fun isWin(): Boolean {
        val enemy = getEnemyEntity()
        val player = userRepository.getReferenceById(userId)

        val gameStatus = gameService.isWin(
            ShotBoard(player.shots.map { it.toEntity() }.toSet().toPositionMap()),
            ShipBoard(enemy.ships.map { it.toEntity() }.toSet(), getGameEntity().gameType)
        )

        if (gameStatus) {
            userRepository.deleteShipsAndShots(player.id)
            userRepository.deleteShipsAndShots(enemy.id)
            gameRepository.delete(getGameEntity())
        }

        return gameStatus
    }

    fun getGameType(): String = getGameEntity().gameType.name

    fun getEnemySankShips(): List<ShipModel>  {
        val enemy = getEnemyEntity()
        val player = userRepository.getReferenceById(userId)

        return gameService.onlySankShips(
            ShotBoard(player.shots.map { it.toEntity() }.toSet().toPositionMap()),
            ShipBoard(enemy.ships.map { it.toEntity() }.toSet(), getGameEntity().gameType)
        ).map { (it as AppShipEntity).toModel() }

    }

    private fun getGameEntity(): GameEntity = gameRepository.getByPlayerId(userId) ?: throw GameNotFoundException()

    private fun getEnemyEntity(): UserEntity {
        val gameEntity = getGameEntity()

        return if (gameEntity.playerA.id == userId) gameEntity.playerB else gameEntity.playerA
    }
}