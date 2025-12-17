package com.douglas.andy.shortener_be.service;

import com.douglas.andy.shortener_be.exception.ShortURLExistsException
import com.douglas.andy.shortener_be.model.ShortenedUrlDAO
import com.douglas.andy.shortener_be.model.UrlEntity
import com.douglas.andy.shortener_be.repository.ShortenedUrlRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*
import kotlin.random.Random
import kotlin.test.assertEquals


@SpringBootTest
class ShortenedUrlServiceImplTest {

    @Autowired
    private lateinit var shortenedUrlServiceImpl: ShortenedUrlService

    @MockitoBean
    lateinit var repository: ShortenedUrlRepository

    @Value("\${shortener.origin}")
    lateinit var origin: String

    val shortenedUrl1 = UrlEntity("fullUrl1" + Random.nextInt(), "shortUrl1" + Random.nextInt())
    val shortenedUrl2 = UrlEntity("fullUrl2" + Random.nextInt(), "shortUrl2" + Random.nextInt())

    @Test
    fun shouldReturnShortenedUrlsDAOsHydratedWithOriginsFromRepo() {

        val shortenedUrl1Hydrated =
            ShortenedUrlDAO(shortenedUrl1.alias, shortenedUrl1.fullUrl, origin + shortenedUrl1.alias)
        val shortenedUrl2Hydrated =
            ShortenedUrlDAO(shortenedUrl2.alias, shortenedUrl2.fullUrl, origin + shortenedUrl2.alias)

        val dataReturnedFromRepository = listOf(shortenedUrl1, shortenedUrl2)
        val expectedDataHydrated =
            listOf(shortenedUrl1Hydrated, shortenedUrl2Hydrated)

        Mockito.`when`(repository.findAll()).thenReturn(dataReturnedFromRepository)

        val actualData = shortenedUrlServiceImpl.findAll()

        assertEquals(expectedDataHydrated, actualData)
        Mockito.verify(repository, Mockito.times(1)).findAll()
    }

    @Test
    fun shouldSavePassedObjectAndReturnItWithHydratedOrigin() {

        val shortenedUrl1Hydrated = UrlEntity(shortenedUrl1.fullUrl, origin + shortenedUrl1.alias)

        Mockito.`when`(repository.save(shortenedUrl1)).thenReturn(shortenedUrl1)
        val actualReturned = shortenedUrlServiceImpl.create(shortenedUrl1);

        assertEquals(shortenedUrl1Hydrated, actualReturned);

        Mockito.verify(repository, Mockito.times(1)).save(shortenedUrl1);
    }

    @Test
    fun shouldThrowExceptionWhenShortUrlAlreadyExists() {

        Mockito.`when`(repository.existsById(shortenedUrl1.alias)).thenReturn(true)

        assertThrows<ShortURLExistsException> {
            shortenedUrlServiceImpl.create(shortenedUrl1);
        }
    }

    @Test
    fun shouldReturnShortenedUrlSearchedFor() {

        Mockito.`when`(repository.findById(shortenedUrl1.alias)).thenReturn(Optional.of(shortenedUrl1))
        val actualReturned = shortenedUrlServiceImpl.findById(shortenedUrl1.alias)

        assertEquals(shortenedUrl1, actualReturned);

        Mockito.verify(repository, Mockito.times(1)).findById(shortenedUrl1.alias)
    }

    @Test
    fun shouldThrowExceptionWhenShortenedUrlNotFound() {

        Mockito.`when`(repository.findById(shortenedUrl1.alias)).thenReturn(Optional.empty())

        assertThrows<NoSuchElementException> {
            shortenedUrlServiceImpl.findById(shortenedUrl1.alias)
        }
    }
}