package ms.kotlin.battleships.app.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import ms.kotlin.battleships.app.exception.InvalidTokenWebSocketException
import ms.kotlin.battleships.app.security.TokenService
import ms.kotlin.battleships.app.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.converter.DefaultContentTypeResolver
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.apache.logging.log4j.LogManager
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.DefaultCsrfToken

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfiguration: WebSocketMessageBrokerConfigurer {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @Autowired
    private lateinit var webSocketAuthService: WebSocketAuthService

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/battleships").setAllowedOrigins("*")
        registry.addEndpoint("/battleships").setAllowedOrigins("*").withSockJS()
    }

    override fun configureMessageConverters(messageConverters: MutableList<MessageConverter>): Boolean {
        return true
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/user", "/queues", "/topic")
        registry.setUserDestinationPrefix("/user/")
        registry.setApplicationDestinationPrefixes("/app/v1")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(
            object: ChannelInterceptor {
                override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
                    val accessor: StompHeaderAccessor? = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
                    if (StompCommand.CONNECT == accessor?.command){
                        val token = accessor.getFirstNativeHeader("Auth-Token") ?: ""
                        logger.debug("Jwt token for websocket is $token")

                        if (token.isNotEmpty()) {
                            val sessionAttributes = SimpMessageHeaderAccessor.getSessionAttributes(message.headers)
                            sessionAttributes?.set(CsrfToken::class.java.name,
                                DefaultCsrfToken("Auth-Token", "Auth-Token", token)
                            )
                            val auth = webSocketAuthService.authenticate(token)

                            SecurityContextHolder.getContext().authentication = auth
                            accessor.user = auth
                        }
                        else {
                            throw InvalidTokenWebSocketException()
                        }

                    }

                    return message
                }
            }
        )
    }
}