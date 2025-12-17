package com.douglas.andy.shortener_be.repository;

import com.douglas.andy.shortener_be.model.UrlEntity
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository

@Repository
interface ShortenedUrlRepository : MongoRepository<UrlEntity, String>