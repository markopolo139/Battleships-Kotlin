package ms.kotlin.battleships.app.services

import ms.kotlin.battleships.app.exception.EmailNotFoundException
import ms.kotlin.battleships.app.exception.UsernameNotFoundException
import ms.kotlin.battleships.app.persistence.repositories.UserRepository
import ms.kotlin.battleships.app.persistence.toEntity
import ms.kotlin.battleships.app.security.AppUserEntity
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService: UserDetailsService {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var  userRepository: UserRepository

    private val userId = (SecurityContextHolder.getContext().authentication as? AppUserEntity)?.id ?: 0

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            logger.error("Username is blank, can't use it to load user from base")
            throw UsernameNotFoundException("")
        }

        return try {
            userRepository.getByUsername(username)?.toEntity() ?: throw UsernameNotFoundException(username)
        }
        catch (ex: UsernameNotFoundException) {
            logger.error("Username $username, can't be found in the base")
            throw ex
        }
    }

    fun loadUserByEmail(email: String): UserDetails = try {
        userRepository.getByEmail(email)?.toEntity() ?: throw EmailNotFoundException(email)
    }
    catch (ex: EmailNotFoundException) {
        logger.error("Email $email, can't be found in the base")
        throw ex
    }

    fun loadUserByUserId(userId: Int): UserDetails = try {
        userRepository.findById(userId).get().toEntity()
    }
    catch (ex: NoSuchElementException) {
        logger.error("User With given id is not found")
        throw ex
    }

}