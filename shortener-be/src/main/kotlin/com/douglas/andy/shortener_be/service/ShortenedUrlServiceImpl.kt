package com.douglas.andy.shortener_be.service

import com.douglas.andy.shortener_be.exception.ShortURLExistsException
import com.douglas.andy.shortener_be.model.ShortenedUrlDAO
import com.douglas.andy.shortener_be.model.UrlEntity
import com.douglas.andy.shortener_be.repository.ShortenedUrlRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ShortenedUrlServiceImpl(private val repository: ShortenedUrlRepository, @Value("\${shortener.origin}") private val origin: String) : ShortenedUrlService {

    override fun create(urlEntity: UrlEntity): UrlEntity {
        if (repository.existsById(urlEntity.alias)) {
            throw ShortURLExistsException()
        };

        val savedItem = repository.save(urlEntity)
        return savedItem.copy(alias = origin + savedItem.alias)
    }

    override fun findAll(): ArrayList<ShortenedUrlDAO> {
        val shortenedURLs = ArrayList<ShortenedUrlDAO>()

        repository.findAll().map { shortenedURLs.add(ShortenedUrlDAO(it.alias, it.fullUrl, origin + it.alias)) }

        return shortenedURLs
    }

    override fun findById(id: String): UrlEntity {
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