package ms.kotlin.battleships.app.exception

class UsernameNotFoundException(username: String): AppException("$username not found(if blank no username was sent)")