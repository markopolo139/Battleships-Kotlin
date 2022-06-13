package ms.kotlin.battleships.app.services

import ms.kotlin.battleships.app.exception.EmailNotFoundException
import ms.kotlin.battleships.app.exception.UsernameNotFoundException
import ms.kotlin.battleships.app.persistence.repositories.UserRepository
import ms.kotlin.battleships.app.persistence.toEntity
import ms.kotlin.battleships.app.security.AppUserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService: UserDetailsService {

    @Autowired
    private lateinit var  userRepository: UserRepository

    private val userId = (SecurityContextHolder.getContext().authentication as? AppUserEntity)?.id ?: 0

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null)
            throw UsernameNotFoundException("")

        return userRepository.getByUsername(username)?.toEntity() ?: throw UsernameNotFoundException(username)
    }

    fun loadUserByEmail(email: String): UserDetails =
        userRepository.getByEmail(email)?.toEntity() ?: throw EmailNotFoundException(email)

}