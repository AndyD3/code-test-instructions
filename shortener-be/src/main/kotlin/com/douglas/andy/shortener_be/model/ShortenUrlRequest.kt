package com.douglas.andy.shortener_be.model

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.URL

data class ShortenUrlRequest (
    @field:NotBlank
    @field:URL
    val fullUrl: String,
    @ValidCustomAlias
    val customAlias: String?,
)