package shopping.global.common

import jakarta.validation.Constraint
import jakarta.validation.Payload
import shopping.global.common.EnumValidator
import kotlin.reflect.KClass

@Constraint(validatedBy = [EnumValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidEnum(
    val enumClass: KClass<out Enum<*>>,
    val message: String = "입력값이 올바르지 않습니다.",
    val nullable: Boolean = false,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
