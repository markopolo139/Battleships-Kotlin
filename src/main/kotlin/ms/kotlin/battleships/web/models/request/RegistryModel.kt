package ms.kotlin.battleships.web.models.request

import ms.kotlin.battleships.web.validators.PasswordValidatorAnnotation
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

class RegistryModel(
    @field:NotBlank @field:Length(min = 5) val username: String,
    @field:NotBlank @field:PasswordValidatorAnnotation val password: String,
    @field:Email @field:NotEmpty val email: String,
)