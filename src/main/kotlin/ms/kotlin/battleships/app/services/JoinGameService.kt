package ms.kotlin.battleships.app.services

import ms.kotlin.battleships.app.exception.GameNotFoundException
import ms.kotlin.battleships.app.exception.InvalidJoinGameTokenException
import ms.kotlin.battleships.app.exception.UserAlreadyInGameException
import ms.kotlin.battleships.app.persistence.entities.GameEntity
import ms.kotlin.battleships.app.persistence.repositories.GameRepository
import ms.kotlin.battleships.app.persistence.repositories.UserRepository
import ms.kotlin.battleships.app.security.AppUserEntity
import ms.kotlin.battleships.app.security.TokenService
import ms.kotlin.battleships.business.value.GameType
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class JoinGameService {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var gameRepository: GameRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var simpMessagingTemplate: SimpMessagingTemplate

    @Autowired
    private lateinit var tokenService: TokenService

    private val userId
        get() = (SecurityContextHolder.getContext()?.authentication?.principal as? AppUserEntity)?.id!!

    fun createJoinGameToken(gameType: String): String {
        validateIfUserIsInGame()
        return tokenService.createJoinGameToken(userId, GameType.valueOf(gameType))
    }

    fun joinGame(joinGameToken: String) {
        validateIfUserIsInGame()
        val gameType = tokenService.getGameTypeFromToken(joinGameToken)

        if (gameType == null) {
            logger.error("Game type wasn't found in joinGameToken")
            throw InvalidJoinGameTokenException()
        }


        val playerA = userRepository.getReferenceById(
            tokenService.extractIdFromToken(joinGameToken) ?: throw InvalidJoinGameTokenException()
        )
        val playerB = userRepository.getReferenceById(userId)

        gameRepository.save(
            GameEntity(0, GameType.valueOf(gameType), playerA, playerB, if(randomTurn()) playerA else playerB)
        )

    }

    fun exitGame() {

        val gameEntity = gameRepository.getByPlayerId(userId) ?: throw GameNotFoundException()

        userRepository.deleteShips(gameEntity.playerA.id)
        userRepository.deleteShots(gameEntity.playerA.id)
        userRepository.deleteShips(gameEntity.playerB.id)
        userRepository.deleteShots(gameEntity.playerB.id)
        gameRepository.delete(gameEntity)

    }

    private fun validateIfUserIsInGame() {
        try {
            if (gameRepository.getByPlayerId(userId) != null) throw UserAlreadyInGameException() else Unit
        }
        catch (ex: UserAlreadyInGameException) {
            logger.error("USer with $userId id is already in another game")
            throw ex
        }
    }

    private fun randomTurn() = Random.nextBoolean()

}