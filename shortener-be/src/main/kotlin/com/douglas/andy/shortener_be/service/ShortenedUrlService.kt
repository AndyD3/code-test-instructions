package com.douglas.andy.shortener_be.service

import com.douglas.andy.shortener_be.model.ShortenedUrlDAO
import com.douglas.andy.shortener_be.model.UrlEntity

interface ShortenedUrlService {
    fun create(urlEntity: UrlEntity): UrlEntity
    fun findAll(): List<ShortenedUrlDAO>
    fun findById(id: String): UrlEntity
    fun deleteById(id: String)
}