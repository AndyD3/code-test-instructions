package com.douglas.andy.shortener_be.service

import com.douglas.andy.shortener_be.model.ShortenedUrl
import com.douglas.andy.shortener_be.repository.ShortenedUrlRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ShortenedUrlServiceImpl(private val repository: ShortenedUrlRepository, @Value("\${shortener.origin}") private val origin: String) : ShortenedUrlService {

    override fun create(shortenedURL: ShortenedUrl): ShortenedUrl {
        val savedItem=repository.save(shortenedURL);
        return savedItem.copy(shortUrl = origin+savedItem.shortUrl)
    }

    override fun findAll(): List<ShortenedUrl> {

        println("origin:"+origin);
        val returnedItems = repository.findAll();

        return returnedItems.map { it.copy(shortUrl = origin+it.shortUrl) }
    }
}