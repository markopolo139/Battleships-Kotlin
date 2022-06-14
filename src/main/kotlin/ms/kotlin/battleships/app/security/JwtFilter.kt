package ms.kotlin.battleships.app.security

import ms.kotlin.battleships.app.services.UserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtFilter(
    private val tokenService: TokenService, private val userService: UserService
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        tokenService.extractIdFromToken()?.also { authenticateFromId(it, request) }
        filterChain.doFilter(request, response)
    }

    private fun authenticateFromId(id: Int, request: HttpServletRequest) {
        val user = userService.loadUserByUserId(id)

        val auth = UsernamePasswordAuthenticationToken(user, null, user.authorities)
        auth.details = WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = auth
    }
}