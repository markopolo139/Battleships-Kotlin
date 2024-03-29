package ms.kotlin.battleships.app.services

import ms.kotlin.battleships.app.exception.EmailNotFoundException
import ms.kotlin.battleships.app.exception.UsernameNotFoundException
import ms.kotlin.battleships.app.persistence.repositories.UserRepository
import ms.kotlin.battleships.app.persistence.toEntity
import ms.kotlin.battleships.app.security.AppUserEntity
import ms.kotlin.battleships.app.utils.toPersistence
import ms.kotlin.battleships.web.models.request.RegistryModel
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService: UserDetailsService {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var  userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    private val userId = (SecurityContextHolder.getContext()?.authentication?.principal as? AppUserEntity)?.id ?: 0

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

    fun createUser(registryModel: RegistryModel) = userRepository.save(
        registryModel.toPersistence().apply {
            password = passwordEncoder.encode(password)
        }
    )

    @PreAuthorize("hasRole('ADMIN')")
    fun deleteByUsername(username: String) {
        userRepository.deleteByUsername(username)
    }

}