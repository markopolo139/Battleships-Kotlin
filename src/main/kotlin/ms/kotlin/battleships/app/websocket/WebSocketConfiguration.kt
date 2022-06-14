package ms.kotlin.battleships.app.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import ms.kotlin.battleships.app.security.TokenService
import ms.kotlin.battleships.app.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.DefaultContentTypeResolver
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfiguration: WebSocketMessageBrokerConfigurer {

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var userService: UserService

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/battleships").setAllowedOrigins("*")
        registry.addEndpoint("/battleships").setAllowedOrigins("*").withSockJS()
    }

    override fun configureMessageConverters(messageConverters: MutableList<MessageConverter>): Boolean {
        return true
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/user/", "/queues/")
        registry.setUserDestinationPrefix("/user/")
        registry.setApplicationDestinationPrefixes("/app/v1")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        super.configureClientInboundChannel(registration)
    }
}