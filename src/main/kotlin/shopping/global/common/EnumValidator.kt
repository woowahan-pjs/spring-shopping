package shopping.global.common

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

class EnumValidator : ConstraintValidator<ValidEnum, Enum<*>> {
    private lateinit var enumClass: KClass<out Enum<*>>
    private var nullable: Boolean = false

    override fun initialize(constraintAnnotation: ValidEnum) {
        this.enumClass = constraintAnnotation.enumClass
        this.nullable = constraintAnnotation.nullable
    }

    override fun isValid(
        value: Enum<*>?,
        p1: ConstraintValidatorContext?,
    ): Boolean {
        if (nullable && value == null) return true

        if (value == null) return false

        return value.javaClass.isAssignableFrom(enumClass.java) || enumClass.java.isAssignableFrom(value.javaClass)
    }
}
