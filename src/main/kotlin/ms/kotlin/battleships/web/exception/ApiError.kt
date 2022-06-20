package ms.kotlin.battleships.web.exception

import org.springframework.http.HttpStatus

data class ApiError(
    val suggestedAction: String,
    val errorMessage: String,
    val subErrors: List<ApiSubError>,
    val httpStatus: HttpStatus
)