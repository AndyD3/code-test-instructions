package com.douglas.andy.shortener_be.model

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.stereotype.Component

@Component
class CustomAliasValidator : ConstraintValidator<ValidCustomAlias?, String?> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {

        if (value.isNullOrEmpty()) {
            return true
        }

        if (value.length !in 6..30) {
            return false
        }

        if (value.contains(Regex("^[A-Za-z0-9$\\-_.+!*'()]+$"))) {
            return true
        }

        return false
    }
}