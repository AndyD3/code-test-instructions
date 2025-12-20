package com.douglas.andy.shortener_be.model

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CustomAliasValidator::class])
annotation class ValidCustomAlias(
    val message: String = "Invalid value for custom alias",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)