package ms.kotlin.battleships.web.exception

data class ApiSubError(
    val suggestedAction: String,
    val errorMessage: String
)