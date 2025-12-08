package com.douglas.andy.shortener_be.model

import org.springframework.data.annotation.Id;

data class ShortenedUrl(
    val fullUrl: String,
    @Id val shortUrl: String
)