package ms.kotlin.battleships.app.persistance.repositories

import ms.kotlin.battleships.app.persistance.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<UserEntity, Int> {

    fun getByUsername(username: String): UserEntity

    fun getByEmail(email: String): UserEntity

    fun getByGameToken(gameToken: String): UserEntity

}