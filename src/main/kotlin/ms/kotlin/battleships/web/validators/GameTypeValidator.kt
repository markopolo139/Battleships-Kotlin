package ms.kotlin.battleships.web.validators

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE,
    AnnotationTarget.CLASS,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CONSTRUCTOR
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [GameTypeValidatorImpl::class])
annotation class GameTypeValidator(
    val message: String = "Invalid game type name",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)