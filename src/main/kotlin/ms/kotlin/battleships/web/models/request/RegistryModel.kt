package ms.kotlin.battleships.web.models.request

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

class RegistryModel(
    @field:NotBlank @field:Length(min = 5) val username: String,
    @field:NotBlank val password: String,
    @field:Email @field:NotEmpty val email: String,
)