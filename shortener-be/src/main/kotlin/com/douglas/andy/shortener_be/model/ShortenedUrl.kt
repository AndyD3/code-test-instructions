package com.douglas.andy.shortener_be.model

data class ShortenedUrl(
    val alias: String,
    val fullUrl: String,
    val shortUrl: String
)