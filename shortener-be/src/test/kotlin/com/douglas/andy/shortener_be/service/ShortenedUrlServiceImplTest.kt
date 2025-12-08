package com.douglas.andy.shortener_be.service;

import com.douglas.andy.shortener_be.exception.ShortURLExistsException
import com.douglas.andy.shortener_be.model.ShortenedUrl
import com.douglas.andy.shortener_be.repository.ShortenedUrlRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.random.Random


@SpringBootTest
class ShortenedUrlServiceImplTest {

    @Autowired
    private lateinit var shortenedUrlServiceImpl: ShortenedUrlService

    @MockitoBean
    lateinit var repository: ShortenedUrlRepository

    @Value("\${shortener.origin}")
    lateinit var origin: String

    val shortenedUrl1 = ShortenedUrl("fullUrl1"+Random.nextInt(), "shortUrl1"+Random.nextInt())
    val shortenedUrl2 = ShortenedUrl("fullUrl2"+Random.nextInt(), "shortUrl2"+Random.nextInt())

    @Test
    fun shouldReturnShortenedUrlsHydratedWithOriginsFromRepo() {

        val shortenedUrl1Hydrated = ShortenedUrl(shortenedUrl1.fullUrl, origin+shortenedUrl1.shortUrl)
        val shortenedUrl2Hydrated = ShortenedUrl(shortenedUrl2.fullUrl, origin+shortenedUrl2.shortUrl)

        val dataReturnedFromRespository = listOf(shortenedUrl1, shortenedUrl2)
        val expectedDataHydrated =
            listOf(shortenedUrl1Hydrated, shortenedUrl2Hydrated)

        Mockito.`when`(repository.findAll()).thenReturn(dataReturnedFromRespository)

        val actualData = shortenedUrlServiceImpl.findAll()

        assertEquals(expectedDataHydrated, actualData)
        Mockito.verify(repository, Mockito.times(1)).findAll()
    }

    @Test
    fun shouldSavePassedObjectAndReturnItWithHydratedOrigin() {

        val shortenedUrl1Hydrated = ShortenedUrl(shortenedUrl1.fullUrl, origin+shortenedUrl1.shortUrl)

        Mockito.`when`(repository.save(shortenedUrl1)).thenReturn(shortenedUrl1)
        val actualReturned = shortenedUrlServiceImpl.create(shortenedUrl1);

        assertEquals(shortenedUrl1Hydrated, actualReturned);

        Mockito.verify(repository, Mockito.times(1)).save(shortenedUrl1);
    }

    @Test
    fun shouldThrowExceptionWhenShortUrlAlreadyExists() {

        Mockito.`when`(repository.existsById(shortenedUrl1.shortUrl)).thenReturn(true);

        assertThrows<ShortURLExistsException> {
            shortenedUrlServiceImpl.create(shortenedUrl1);
        }
    }

    @Test
    fun shouldReturnShortenedUrlSearchedFor() {

        Mockito.`when`(repository.findById(shortenedUrl1.shortUrl)).thenReturn(Optional.of(shortenedUrl1))
        val actualReturned = shortenedUrlServiceImpl.findById(shortenedUrl1.shortUrl);

        assertEquals(shortenedUrl1, actualReturned);

        Mockito.verify(repository, Mockito.times(1)).findById(shortenedUrl1.shortUrl);
    }

    @Test
    fun shouldThrowExceptionWhenShortenedUrlNotFound() {

        Mockito.`when`(repository.findById(shortenedUrl1.shortUrl)).thenReturn(Optional.empty())

        assertThrows<NoSuchElementException> {
            shortenedUrlServiceImpl.findById(shortenedUrl1.shortUrl)
        }
    }
}