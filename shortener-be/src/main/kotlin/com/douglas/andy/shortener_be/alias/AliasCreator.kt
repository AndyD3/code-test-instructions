package com.douglas.andy.shortener_be.alias

import com.douglas.andy.shortener_be.exception.ShortURLExistsException
import com.douglas.andy.shortener_be.model.ShortenUrlRequest
import com.douglas.andy.shortener_be.model.UrlEntity
import com.douglas.andy.shortener_be.service.ShortenedUrlService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component

@Component
class AliasCreator(
    private val aliasGenerator: AliasGenerator,
    private val service: ShortenedUrlService,
    @Value("\${shortener.alias.creation-retries}") private val retries: Int
) {
    var logger: Logger = LoggerFactory.getLogger(AliasCreator::class.java)

    fun create(request: ShortenUrlRequest): UrlEntity {
        if (!request.customAlias.isNullOrEmpty()) {
            try {
                return service.create(UrlEntity(request.fullUrl, request.customAlias))
            } catch (_: DuplicateKeyException) {
                logger.warn("Duplicate key on custom alias ${request.customAlias}")
                throw ShortURLExistsException()
            }
        }
        repeat(retries + 1) {
            try {
                return service.create(UrlEntity(request.fullUrl, aliasGenerator.generate()))
            } catch (_: DuplicateKeyException) {
                logger.warn("Duplicate key on random alias");
            }
        }
        logger.error("Duplicate key/alias exception trying ${retries+1} randomized aliases/keys")
        throw ShortURLExistsException()
    }
}
