package ms.kotlin.battleships.web.exception

import ms.kotlin.battleships.business.exception.*
import ms.kotlin.battleships.business.value.Position
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

@Suppress("UNCHECKED_CAST")
@ControllerAdvice
class ExceptionHandler: ResponseEntityExceptionHandler() {

    companion object {
        const val DEFAULT_ACTION = "Contact server admin"
        const val DEFAULT_ERROR_MESSAGE = "Unexpected error occurred"
        val DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR
    }

    @Autowired
    private lateinit var request: HttpServletRequest

    private fun ApiError.toResponseEntity() = ResponseEntity<ApiError>(this, httpStatus)

    private fun error(
        action: String = DEFAULT_ACTION,
        error: String = DEFAULT_ERROR_MESSAGE,
        apiSubErrors: List<ApiSubError> = listOf(),
        httpStatus: HttpStatus = DEFAULT_HTTP_STATUS
    ): ResponseEntity<ApiError> = ApiError(action, error, apiSubErrors, httpStatus).toResponseEntity()

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return error(
            action = "Give needed request body",
            error = ex.message ?: "Required request body",
            httpStatus = HttpStatus.BAD_REQUEST
        ) as ResponseEntity<Any>
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val subApiErrors: List<ApiSubError> = ex.bindingResult.fieldErrors.asSequence().map {
            ApiSubError(
                errorMessage = "validation failed for ${it.rejectedValue} - ${it.defaultMessage}",
                suggestedAction = "Check rejected value"
            )
        }.toList()

        return error(
            action = "Check error sublist for more information",
            error = "Error occurred during validation",
            apiSubErrors = subApiErrors,
            httpStatus = HttpStatus.BAD_REQUEST
        ) as ResponseEntity<Any>
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationExceptionHandler(ex: ConstraintViolationException): ResponseEntity<ApiError> {
        val subApiErrors: List<ApiSubError> = ex.constraintViolations.asSequence().map {
            ApiSubError(
                errorMessage = "validation failed for ${it.invalidValue} - ${it.message}",
                suggestedAction = "Check rejected value"
            )
        }.toList()

        return error(
            action = "Check error sublist for more information",
            error = "Error occurred during validation",
            apiSubErrors = subApiErrors,
            httpStatus = HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun entityNotFoundExceptionHandler(ex: EntityNotFoundException): ResponseEntity<ApiError> =
        error(
            action = "Give valid entity information",
            error = ex.message ?: "Entity not found in database",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(InvalidLayoutException::class)
    fun invalidLayoutExceptionHandler(ex: InvalidLayoutException): ResponseEntity<ApiError> =
        error(
            action = "Check again ships placement",
            error = ex.message ?: "Invalid placement of ships",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(InvalidShotException::class)
    fun invalidShotExceptionHandler(ex: InvalidShotException): ResponseEntity<ApiError> =
        error(
            action = "Select position which does not occurred before",
            error = ex.message ?: "Selected invalid position",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(PositionOutOfRangeException::class)
    fun positionOutOfRangeExceptionHandler(ex: PositionOutOfRangeException): ResponseEntity<ApiError> =
        error(
            action = "Select position which is in range (min = ${Position.MINIMAL}, max = ${Position.MAXIMUM})",
            error = ex.message ?: "Selected invalid position",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(ShipContinuityException::class)
    fun shipContinuityExceptionHandler(ex: ShipContinuityException): ResponseEntity<ApiError> =
        error(
            action = "Place ship correctly",
            error = ex.message ?: "Ship is not continuous",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(ShipInvalidRowException::class)
    fun shipInvalidRowExceptionHandler(ex: ShipInvalidRowException): ResponseEntity<ApiError> =
        error(
            action = "Place ship correctly",
            error = ex.message ?: "Ship is not placed in one row",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(ShotNotFoundException::class)
    fun shotNotFoundExceptionHandler(ex: ShotNotFoundException): ResponseEntity<ApiError> =
        error(
            action = "Get shot which occurred before",
            error = ex.message ?: "In selected position shot does not exists",
            httpStatus = HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(InvalidShipException::class)
    fun invalidShipExceptionHandler(ex: InvalidShipException): ResponseEntity<ApiError> =
        error(
            action = "Check ship placement",
            error = ex.message ?: "Ship is invalid",
            httpStatus = HttpStatus.BAD_REQUEST
        )

}
