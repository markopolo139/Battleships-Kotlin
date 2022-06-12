package ms.kotlin.battleships.app.entities

import ms.kotlin.battleships.app.security.AppUserEntity
import ms.kotlin.battleships.business.value.GameType

data class AppGameEntity(
    val id: Int,
    val gameType: GameType,
    val playerA: AppUserEntity,
    val playerB: AppUserEntity,
    val currentPlayer: AppUserEntity,
)