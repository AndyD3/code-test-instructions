package com.douglas.andy.shortener_be.model

import org.springframework.data.annotation.Id;

data class UrlEntity(
    val fullUrl: String,
    @Id val alias: String
)