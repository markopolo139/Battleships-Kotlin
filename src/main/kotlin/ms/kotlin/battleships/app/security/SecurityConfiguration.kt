package ms.kotlin.battleships.app.security

import ms.kotlin.battleships.app.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AnonymousAuthenticationProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfiguration {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Bean
    fun securityFilterChain(http: HttpSecurity?): SecurityFilterChain? {
        if (http == null) return null

        http.csrf()
            .disable()
            .cors()
            .and()
            .anonymous().principal("anonymous").authorities("ROLE_GUEST")
            .and()
            .addFilterBefore(JwtFilter(tokenService, userService), UsernamePasswordAuthenticationFilter::class.java)
            .userDetailsService(userService)
            .authorizeRequests()
            .antMatchers("/auth").permitAll()
            .antMatchers("/register").permitAll()
            .antMatchers("/recoverpwd").permitAll()
            .antMatchers("/battleships").permitAll()
            .anyRequest().authenticated()


        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        return http.build()
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager? {
        return config.authenticationManager
    }

    @Bean
    @Scope(value = "singleton")
    fun authenticationProvider(): AuthenticationProvider {
        val daoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder)
        daoAuthenticationProvider.setUserDetailsService(userService)

        return daoAuthenticationProvider
    }

    @Bean
    @Scope(value = "singleton")
    fun getHierarchy(): RoleHierarchy {
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy(
            "ROLE_ADMIN > ROLE_USER\nROLE_USER > ROLE_GUEST"
        )

        return roleHierarchy
    }

    @Bean
    @Scope(value = "singleton")
    fun getAnonymousProvider() = AnonymousAuthenticationProvider("anonymous")

    @Bean
    @Scope(value = "singleton")
    fun getAuthenticationFilter() =
        AnonymousAuthenticationFilter("anonymous", "anonymous", listOf(SimpleGrantedAuthority("ROLE_GUEST")))


}