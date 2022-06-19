package ms.kotlin.battleships.app.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class PasswordEncoderConf {

    @Bean
    fun getPasswordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

}