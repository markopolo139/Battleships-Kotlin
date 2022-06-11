package ms.kotlin.battleships.app.entities

import ms.kotlin.battleships.business.value.GameType

data class GameEntity(
    val id: Int,
    val gameType: GameType,
    val playerA: UserEntity,
    val playerB: UserEntity,
    val currentPlayer: UserEntity,
)