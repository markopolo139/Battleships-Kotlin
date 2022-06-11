package ms.kotlin.battleships.app.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer

@Configuration
class WebSocketSecurityConfiguration: AbstractSecurityWebSocketMessageBrokerConfigurer() {

    override fun sameOriginDisabled(): Boolean {
        return true
    }

    override fun configureInbound(messages: MessageSecurityMetadataSourceRegistry?) {
        messages?.anyMessage()?.permitAll()
    }
}