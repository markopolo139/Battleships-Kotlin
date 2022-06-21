package ms.kotlin.battleships.web.validators

import ms.kotlin.battleships.business.value.GameType
import ms.kotlin.battleships.business.value.ShotType
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class GameTypeValidatorImpl: ConstraintValidator<GameTypeValidator, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return try {
            GameType.valueOf(value ?: "")
            true
        } catch (ex: Exception) {
            false
        }
    }
}