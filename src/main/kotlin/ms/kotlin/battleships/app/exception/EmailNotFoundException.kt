package ms.kotlin.battleships.app.exception

class EmailNotFoundException(email: String): Exception("$email not found")