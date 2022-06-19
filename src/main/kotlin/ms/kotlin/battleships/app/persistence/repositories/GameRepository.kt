package ms.kotlin.battleships.app.persistence.repositories

import ms.kotlin.battleships.app.persistence.entities.GameEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface GameRepository: JpaRepository<GameEntity, Int> {

    @Query("Select ge from GameEntity ge where ge.playerA.id = :id or ge.playerB.id = :id")
    fun getByPlayerId(@Param(value = "id") playerId: Int): GameEntity?

}