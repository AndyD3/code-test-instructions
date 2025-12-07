package com.douglas.andy.shortener_be.service;

import com.douglas.andy.shortener_be.model.ShortenedUrl
import com.douglas.andy.shortener_be.repository.ShortenedUrlRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import kotlin.test.assertEquals

@SpringBootTest
class ShortenedUrlServiceImplTest {

    @Autowired
    private lateinit var shortenedUrlServiceImpl: ShortenedUrlService

    @MockitoBean
    lateinit var repository : ShortenedUrlRepository

    @Test
    fun shouldPassBackAllDataFromRepository() {

        val shortenedUrl = ShortenedUrl("fullUrl1", "shortUrl1")
        val shortenedUrl2 = ShortenedUrl("fullUrl2", "shortUrl2")
        val expectedList = listOf(shortenedUrl, shortenedUrl2)

        Mockito.`when`(repository.findAll()).thenReturn(expectedList)

        val actualList= shortenedUrlServiceImpl.findAll()

        assertEquals(actualList,actualList)
        Mockito.verify(repository, Mockito.times(1)).findAll()
    }

    @Test
    fun shouldSavePassedObject() {

        val shortenedUrl = ShortenedUrl("fullUrl1", "shortUrl1")

        Mockito.`when`(repository.save(shortenedUrl)).thenReturn(shortenedUrl)
        shortenedUrlServiceImpl.create(shortenedUrl);

        Mockito.verify(repository, Mockito.times(1)).save(shortenedUrl);
    }
}