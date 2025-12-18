package com.douglas.andy.shortener_be.alias

import com.douglas.andy.shortener_be.exception.ShortURLExistsException
import com.douglas.andy.shortener_be.model.ShortenUrlRequest
import com.douglas.andy.shortener_be.model.UrlEntity
import com.douglas.andy.shortener_be.service.ShortenedUrlService
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.bean.override.mockito.MockitoBean
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class AliasCreatorTest {

    @Autowired
    private lateinit var aliasCreator: AliasCreator

    @MockitoBean
    private lateinit var service: ShortenedUrlService

    @MockitoBean
    private lateinit var aliasGenerator: AliasGenerator

    @Test
    fun shouldCallCreateOnServiceWithCustomAliasSupplied() {

        val stubbedUrlL = "fullUrl1" + Random.nextInt()
        val stubbedAlias = "stubbedAlias" + Random.nextInt()
        val shortenedUrl = UrlEntity(stubbedUrlL, stubbedAlias)
        val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, stubbedAlias)

        Mockito.`when`(service.create(shortenedUrl)).thenReturn(shortenedUrl)

        var result = aliasCreator.create(shortenUrlRequest)

        assertEquals(shortenedUrl, result)

        Mockito.verify(service, Mockito.times(1)).create(shortenedUrl)
    }

    @Test
    fun shouldThrowExceptionWhenCustomAliasAlreadyExists() {

        val stubbedUrlL = "fullUrl1" + Random.nextInt()
        val stubbedAlias = "stubbedAlias" + Random.nextInt()
        val shortenedUrl = UrlEntity(stubbedUrlL, stubbedAlias)
        val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, stubbedAlias)

        Mockito.`when`(service.create(shortenedUrl)).thenThrow(DuplicateKeyException("Alias already exists"))

        assertThrows<ShortURLExistsException> {
            aliasCreator.create(shortenUrlRequest)
        }

        Mockito.verify(service, Mockito.times(1)).create(shortenedUrl)
    }

    @Test
    fun shouldCreateAnAliasAndCallCreateWithIt() {

        val stubbedUrlL = "fullUrl1" + Random.nextInt()
        val stubbedAlias = "stubbedAlias" + Random.nextInt()
        val shortenedUrl = UrlEntity(stubbedUrlL, stubbedAlias)
        val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, null)

        Mockito.`when`(service.create(shortenedUrl)).thenReturn(shortenedUrl)
        Mockito.`when`(aliasGenerator.generate()).thenReturn(stubbedAlias)

        var result = aliasCreator.create(shortenUrlRequest)

        assertEquals(shortenedUrl, result)

        Mockito.verify(aliasGenerator, Mockito.times(1)).generate()
        Mockito.verify(service, Mockito.times(1)).create(shortenedUrl)
    }

    @Test
    fun shouldRepeatedlyCreateAliasAndTryToCreateWhenAliasExists() {
        val stubbedUrlL = "fullUrl1" + Random.nextInt()
        val stubbedAlias = "stubbedAlias" + Random.nextInt()
        val stubbedAlias2 = "stubbedAlias" + Random.nextInt()
        val shortenedUrl1 = UrlEntity(stubbedUrlL, stubbedAlias)
        val shortenedUrl2 = UrlEntity(stubbedUrlL, stubbedAlias2)
        val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, null)

        Mockito.`when`(service.create(shortenedUrl1))
            .thenThrow(DuplicateKeyException("Alias already exists"))

        Mockito.`when`(service.create(shortenedUrl2))
            .thenReturn(shortenedUrl2)

        Mockito.`when`(aliasGenerator.generate())
            .thenReturn(stubbedAlias)
            .thenReturn(stubbedAlias2)

        var result = aliasCreator.create(shortenUrlRequest)

        assertEquals(shortenedUrl2, result)

        Mockito.verify(aliasGenerator, Mockito.times(2)).generate()
        Mockito.verify(service, Mockito.times(1)).create(shortenedUrl1)
        Mockito.verify(service, Mockito.times(1)).create(shortenedUrl2)
    }

    @Test
    fun shouldRepeatedlyCreateAliasAndTryToCreateWhenAliasExistsAndFailAfterSpecifiedRetries() {
        val stubbedUrlL = "fullUrl1" + Random.nextInt()
        val stubbedAlias = "stubbedAlias" + Random.nextInt()
        val stubbedAlias2 = "stubbedAlias" + Random.nextInt()
        val stubbedAlias3 = "stubbedAlias" + Random.nextInt()
        val shortenedUrl1 = UrlEntity(stubbedUrlL, stubbedAlias)
        val shortenedUrl2 = UrlEntity(stubbedUrlL, stubbedAlias2)
        val shortenedUrl3 = UrlEntity(stubbedUrlL, stubbedAlias3)
        val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, null)

        Mockito.`when`(service.create(shortenedUrl1))
            .thenThrow(DuplicateKeyException("Alias already exists"))

        Mockito.`when`(service.create(shortenedUrl2))
            .thenThrow(DuplicateKeyException("Alias already exists"))

        Mockito.`when`(service.create(shortenedUrl3))
            .thenThrow(DuplicateKeyException("Alias already exists"))

        Mockito.`when`(aliasGenerator.generate())
            .thenReturn(stubbedAlias)
            .thenReturn(stubbedAlias2)
            .thenReturn(stubbedAlias3)

        assertThrows<ShortURLExistsException> {
            aliasCreator.create(shortenUrlRequest)
        }

        Mockito.verify(aliasGenerator, Mockito.times(3)).generate()
        Mockito.verify(service, Mockito.times(1)).create(shortenedUrl1)
        Mockito.verify(service, Mockito.times(1)).create(shortenedUrl2)
        Mockito.verify(service, Mockito.times(1)).create(shortenedUrl3)
    }
}