package ms.kotlin.battleships.app.exception

import org.springframework.mail.MailException

class MailCreationException: MailException("Error during mail creation")