package com.douglas.andy.shortener_be.service

import com.douglas.andy.shortener_be.model.ShortenedUrl
import com.douglas.andy.shortener_be.exception.ShortURLExistsException
import com.douglas.andy.shortener_be.repository.ShortenedUrlRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ShortenedUrlServiceImpl(private val repository: ShortenedUrlRepository, @Value("\${shortener.origin}") private val origin: String) : ShortenedUrlService {

    override fun create(shortenedURL: ShortenedUrl): ShortenedUrl {
        if(repository.existsById(shortenedURL.shortUrl)) {
            throw ShortURLExistsException()
        };

        val savedItem=repository.save(shortenedURL)
        return savedItem.copy(shortUrl = origin+savedItem.shortUrl)
    }

    override fun findAll(): List<ShortenedUrl> {
        val returnedItems = repository.findAll();
        return returnedItems.map { it.copy(shortUrl = origin+it.shortUrl) }
    }

    override fun findById(id: String) : ShortenedUrl {
        val foundItem=repository.findById(id);
        return foundItem.orElseThrow();
    }

    override fun deleteById(id: String) {
        if(!repository.existsById(id)) {
            throw NoSuchElementException()
        };

        repository.deleteById(id);
    }
}