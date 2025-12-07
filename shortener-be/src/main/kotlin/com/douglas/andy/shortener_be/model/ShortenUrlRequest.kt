package com.douglas.andy.shortener_be.model

data class ShortenUrlRequest (
    val fullUrl: String,
    val customAlias: String?,
)