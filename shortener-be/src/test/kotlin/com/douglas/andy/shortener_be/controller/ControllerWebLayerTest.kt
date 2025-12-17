package com.douglas.andy.shortener_be.controller;

import com.douglas.andy.shortener_be.alias.AliasGenerator
import com.douglas.andy.shortener_be.exception.ShortURLExistsException
import com.douglas.andy.shortener_be.model.ShortenUrlRequest
import com.douglas.andy.shortener_be.model.ShortenedUrlDAO
import com.douglas.andy.shortener_be.model.UrlEntity
import com.douglas.andy.shortener_be.service.ShortenedUrlService
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
class TestingWebApplicationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapper();

    @MockitoBean
    private lateinit var service: ShortenedUrlService

    @MockitoBean
    private lateinit var aliasGenerator: AliasGenerator

    val shortenedUrl1 = UrlEntity("fullUrl1" + Random.nextInt(), "shortUrl1" + Random.nextInt())

    val shortenedUrlDAO1 =
        ShortenedUrlDAO("alias1" + Random.nextInt(), "fullUrl1" + Random.nextInt(), "shortUrl1" + Random.nextInt())
    val shortenedUrlDAO2 =
        ShortenedUrlDAO("alias2" + Random.nextInt(), "fullUrl2" + Random.nextInt(), "shortUrl2" + Random.nextInt())

    @Test
    fun shouldGetAllAsList() {

        val urlList = listOf(shortenedUrlDAO1, shortenedUrlDAO2)

        Mockito.`when`(service.findAll()).thenReturn(urlList)

        mockMvc.perform(get("/urls"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize<UrlEntity>(2)))
            .andExpect(jsonPath("$[0].alias", `is`(shortenedUrlDAO1.alias)))
            .andExpect(jsonPath("$[0].fullUrl", `is`(shortenedUrlDAO1.fullUrl)))
            .andExpect(jsonPath("$[0].shortUrl", `is`(shortenedUrlDAO1.shortUrl)))
            .andExpect(jsonPath("$[1].alias", `is`(shortenedUrlDAO2.alias)))
            .andExpect(jsonPath("$[1].fullUrl", `is`(shortenedUrlDAO2.fullUrl)))
            .andExpect(jsonPath("$[1].shortUrl", `is`(shortenedUrlDAO2.shortUrl)))

        Mockito.verify(service, Mockito.times(1)).findAll()
    }

    @Test
    fun shouldCreateWithAliasAndReturnCreatedShortUrlWithDomain() {

        val shortenUrlRequest = ShortenUrlRequest(fullUrl = shortenedUrl1.fullUrl, customAlias = "customAlias")
        val shortenedUrl = UrlEntity(shortenedUrl1.fullUrl, "customAlias")

        Mockito.`when`(service.create(shortenedUrl)).thenReturn(shortenedUrl1)

        mockMvc.perform(
            post("/shorten")
                .content(objectMapper.writeValueAsString(shortenUrlRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.shortUrl", `is`(shortenedUrl1.alias)))

        Mockito.verify(service, Mockito.times(1)).create(shortenedUrl)
    }

    @Test
    fun shouldCreateWithoutAliasAndReturnCreatedShortUrlWithDomain() {

        val shortenUrlRequest = ShortenUrlRequest(fullUrl = shortenedUrl1.fullUrl, customAlias = null)
        val stubbedAlias = "stubbedAlias" + Random.nextInt();

        val shortenedUrl = UrlEntity(shortenedUrl1.fullUrl, stubbedAlias)

        Mockito.`when`(aliasGenerator.generate()).thenReturn(stubbedAlias)
        Mockito.`when`(service.create(shortenedUrl)).thenReturn(shortenedUrl1)

        mockMvc.perform(
            post("/shorten")
                .content(objectMapper.writeValueAsString(shortenUrlRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.shortUrl", `is`(shortenedUrl1.alias)))

        Mockito.verify(service, Mockito.times(1)).create(shortenedUrl)
    }


    @Test
    fun shouldReturnErrorWhenCustomAliasAlreadyExists() {

        val shortenUrlRequest = ShortenUrlRequest(shortenedUrl1.fullUrl, "customAlias")
        val shortenedUrl = UrlEntity(shortenedUrl1.fullUrl, "customAlias")

        Mockito.`when`(service.create(shortenedUrl)).thenThrow(ShortURLExistsException())

        mockMvc.perform(
            post("/shorten")
                .content(objectMapper.writeValueAsString(shortenUrlRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isBadRequest)
            .andExpect(content().string("Invalid input or alias already taken"))

        Mockito.verify(service, Mockito.times(1)).create(shortenedUrl)
    }

    @Test
    fun shouldRedirectToLongUrl() {

        Mockito.`when`(service.findById(shortenedUrl1.alias)).thenReturn(shortenedUrl1)

        mockMvc.perform(get("/" + shortenedUrl1.alias))
            .andDo(print())
            .andExpect(status().isFound)
            .andExpect(redirectedUrl(shortenedUrl1.fullUrl));

        Mockito.verify(service, Mockito.times(1)).findById(shortenedUrl1.alias)
    }

    @Test
    fun shouldReturnNotFoundWhenShortUrlNotFound() {

        Mockito.`when`(service.findById("notExists")).thenThrow(NoSuchElementException());

        mockMvc.perform(get("/notExists"))
            .andDo(print())
            .andExpect(status().isNotFound)
            .andExpect(content().string("Alias not found"))

        Mockito.verify(service, Mockito.times(1)).findById("notExists");
    }

    @Test
    fun shouldDeleteShortURL() {

        mockMvc.perform(delete("/" + shortenedUrl1.alias))
            .andDo(print())
            .andExpect(status().isNoContent)

        Mockito.verify(service, Mockito.times(1)).deleteById(shortenedUrl1.alias)
    }

    @Test
    fun shouldReturnNotFoundWhenShortUrlNotFoundForDeletion() {

        Mockito.`when`(service.deleteById("notExists")).thenThrow(NoSuchElementException());

        mockMvc.perform(delete("/notExists"))
            .andDo(print())
            .andExpect(status().isNotFound)
            .andExpect(content().string("Alias not found"))

        Mockito.verify(service, Mockito.times(1)).deleteById("notExists");
    }
}