package com.douglas.andy.shortener_be.service

import com.douglas.andy.shortener_be.model.ShortenedUrl

interface ShortenedUrlService {

    fun create(request: ShortenedUrl): ShortenedUrl
    fun findAll(): List<ShortenedUrl>
    fun findById(id: String) : ShortenedUrl
    fun deleteById(id: String)
}