package com.douglas.andy.shortener_be.controller;

import com.douglas.andy.shortener_be.model.ShortenUrlRequest
import com.douglas.andy.shortener_be.model.ShortenUrlResponse
import com.douglas.andy.shortener_be.model.ShortenedUrl
import com.douglas.andy.shortener_be.service.ShortenedUrlService
import com.douglas.andy.shortener_be.util.ObjectMapperUtil
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.hamcrest.Matchers.hasSize
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class TestingWebApplicationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapperUtil = ObjectMapperUtil()

    @MockitoBean
    private lateinit var service: ShortenedUrlService

    @Test
    fun shouldGetAllAsList() {
        val shortenedUrl = ShortenedUrl("fullUrl1", "shortUrl1")
        val shortenedUrl2 = ShortenedUrl("fullUrl2", "shortUrl2")

        val urlList = listOf(shortenedUrl, shortenedUrl2)

        Mockito.`when`(service.findAll()).thenReturn(urlList)

        mockMvc.perform(get("/urls"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize<ShortenedUrl>(2)))
                .andExpect(jsonPath("$[0].fullUrl", `is`(shortenedUrl.fullUrl)))
            .andExpect(jsonPath("$[0].shortUrl", `is`(shortenedUrl.shortUrl)))
            .andExpect(jsonPath("$[1].fullUrl", `is`(shortenedUrl2.fullUrl)))
            .andExpect(jsonPath("$[1].shortUrl", `is`( shortenedUrl2.shortUrl)))

        Mockito.verify(service, Mockito.times(1)).findAll()
    }

    @Test
    fun shouldCreateWithAliasAndReturnCreatedShortUrlWithDomain() {

        val shortenedUrl = ShortenedUrl("fullURL", "customAlias")
        val shortenUrlRequest = ShortenUrlRequest("fullURL", "customAlias")

        Mockito.`when`(service.create(shortenedUrl)).thenReturn(shortenedUrl)

        mockMvc.perform(
                        post("/shorten")
                                .content(objectMapperUtil.getJson(shortenUrlRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.shortUrl", `is`(shortenedUrl.shortUrl)))

        Mockito.verify(service, Mockito.times(1)).create(shortenedUrl)
    }
}