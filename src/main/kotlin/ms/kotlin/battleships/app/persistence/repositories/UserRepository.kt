package ms.kotlin.battleships.app.persistence.repositories

import ms.kotlin.battleships.app.persistence.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository: JpaRepository<UserEntity, Int> {

    fun getByUsername(username: String): UserEntity?

    fun getByEmail(email: String): UserEntity?

    fun getByGameToken(gameToken: String): UserEntity?

    @Modifying
    @Transactional
    @Query(value = "delete from s1, s2 using shots s1 inner join ships s2 on s1.players_id = s2.players_id" +
            " where s1.players_id = :playerId", nativeQuery = true)
    fun deleteShipsAndShots(@Param(value = "playerId") playerId: Int)

    @Modifying
    @Transactional
    fun deleteByUsername(username: String)

}