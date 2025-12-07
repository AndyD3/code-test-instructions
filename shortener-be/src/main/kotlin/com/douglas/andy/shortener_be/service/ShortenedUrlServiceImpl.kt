package com.douglas.andy.shortener_be.service

import com.douglas.andy.shortener_be.model.ShortenedUrl
import com.douglas.andy.shortener_be.repository.ShortenedUrlRepository
import org.springframework.stereotype.Service

@Service
class ShortenedUrlServiceImpl(private val repository: ShortenedUrlRepository) : ShortenedUrlService {

    override fun create(shortenedURL: ShortenedUrl): ShortenedUrl {
        System.out.println("in service"+shortenedURL);
        return repository.save(shortenedURL);
    }

    override fun findAll(): List<ShortenedUrl> = repository.findAll();
}