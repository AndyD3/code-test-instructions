package com.douglas.andy.shortener_be.service;

import com.douglas.andy.shortener_be.model.ShortenedUrl
import com.douglas.andy.shortener_be.repository.ShortenedUrlRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import kotlin.test.assertEquals

@SpringBootTest
class ShortenedUrlServiceImplTest {

    @Autowired
    private lateinit var shortenedUrlServiceImpl: ShortenedUrlService

    @MockitoBean
    lateinit var repository : ShortenedUrlRepository

    @Value("\${shortener.origin}")
    lateinit var origin : String

    @Test
    fun shouldPassBackShortenedUrlAndHydratingWithTheOriginDataFromRepository() {

        val dataReturnedFromRespository = listOf(ShortenedUrl("fullUrl1", "shortUrl1"), ShortenedUrl("fullUrl2", "shortUrl2"))
        val expectedDataHydrated = listOf(ShortenedUrl("fullUrl1",origin+"shortUrl1"), ShortenedUrl("fullUrl2", origin+"shortUrl2"))

        Mockito.`when`(repository.findAll()).thenReturn(dataReturnedFromRespository)

        val actualData= shortenedUrlServiceImpl.findAll()

        assertEquals(expectedDataHydrated, actualData)
        Mockito.verify(repository, Mockito.times(1)).findAll()
    }

    @Test
    fun shouldSavePassedObjectAndReturnItWithHydratedOrigin() {

        val shortenedUrl = ShortenedUrl("fullUrl1", "shortUrl1")
        val expectedReturned = ShortenedUrl("fullUrl1", origin+"shortUrl1")

        Mockito.`when`(repository.save(shortenedUrl)).thenReturn(shortenedUrl)
        val actualReturned = shortenedUrlServiceImpl.create(shortenedUrl);

        assertEquals(expectedReturned,actualReturned);

        Mockito.verify(repository, Mockito.times(1)).save(shortenedUrl);
    }
}