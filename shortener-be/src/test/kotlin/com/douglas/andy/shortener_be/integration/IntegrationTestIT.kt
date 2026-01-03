package com.douglas.andy.shortener_be.integration

import com.douglas.andy.shortener_be.model.ShortenUrlRequest
import com.douglas.andy.shortener_be.model.UrlEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.random.Random

/*
    This runs against the real database and therefore will only pass when it is up and running.

    This requires the database to be empty - so no accidents by executing it

    To execute use:
        mvn failsafe:integration-test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntegrationTestIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Value("\${shortener.origin}")
    lateinit var origin: String

    @Autowired
    var mongoTmpl: MongoTemplate? = null

    private val objectMapper = ObjectMapper()

    val stubbedUrlL = "https://en.wikipedia.org/wiki/Doge_(meme)" + Random.nextInt()
    val stubbedAlias = "stubbedAlias" + Random.nextInt()

    @BeforeAll
    fun ensureDBEmpty() {
        val all = mongoTmpl?.findAll<UrlEntity>(UrlEntity::class.java)
        assert(all.isNullOrEmpty())
    }

    @AfterEach
    fun dropCollection() {
        mongoTmpl?.dropCollection("urlEntity")
    }

    fun shortenUrl(shortenUrlRequest: ShortenUrlRequest) {
        mockMvc.perform(
            post("/shorten")
                .content(objectMapper.writeValueAsString(shortenUrlRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
    }

    @Nested
    @DisplayName("/shorten")
    inner class TestShorten {

        @Test
        fun shouldCreateAndReturnSuccessWithValidUrlAndWithoutAlias() {

            val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, null)

            mockMvc.perform(
                post("/shorten")
                    .content(objectMapper.writeValueAsString(shortenUrlRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated)
                .andExpect(MockMvcResultMatchers.jsonPath("$.shortUrl", Matchers.notNullValue()))
        }

        @Test
        fun shouldCreateAndReturnSuccessWithValidURLAndAlias() {

            val shortenedUrl1 = UrlEntity(stubbedUrlL, stubbedAlias)
            val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, stubbedAlias)

            mockMvc.perform(
                post("/shorten")
                    .content(objectMapper.writeValueAsString(shortenUrlRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.shortUrl", `is`(origin + shortenedUrl1.alias)))

        }

        @Test
        fun shouldReturnErrorWhenCustomAliasAlreadyExists() {

            val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, stubbedAlias)

            shortenUrl(shortenUrlRequest)

            mockMvc.perform(
                post("/shorten")
                    .content(objectMapper.writeValueAsString(shortenUrlRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
                .andExpect(content().string("Invalid input or alias already taken"))
        }

        @Test
        fun shouldReturnValidationErrorWhenValidationRulesBroken() {
            val notValidUrl = "thisIsNotAValidUrl"

            val shortenUrlRequest = ShortenUrlRequest(notValidUrl, stubbedAlias)

            mockMvc.perform(
                post("/shorten")
                    .content(objectMapper.writeValueAsString(shortenUrlRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
                .andExpect(content().string("Invalid input or alias already taken"))
        }
    }

    @Test
    fun shouldGetAllAsList() {

        val shortenUrlRequest =
            ShortenUrlRequest(stubbedUrlL + Random.nextInt(), "shortUrl1" + Random.nextInt())
        val shortenUrlRequest2 =
            ShortenUrlRequest(stubbedUrlL + Random.nextInt(), "shortUrl2" + Random.nextInt())

        shortenUrl(shortenUrlRequest)
        shortenUrl(shortenUrlRequest2)

        mockMvc.perform(get("/urls"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize<UrlEntity>(2)))
            .andExpect(jsonPath("$[0].alias", `is`(shortenUrlRequest.customAlias)))
            .andExpect(jsonPath("$[0].fullUrl", `is`(shortenUrlRequest.fullUrl)))
            .andExpect(jsonPath("$[0].shortUrl", `is`(origin + shortenUrlRequest.customAlias)))
            .andExpect(jsonPath("$[1].alias", `is`(shortenUrlRequest2.customAlias)))
            .andExpect(jsonPath("$[1].fullUrl", `is`(shortenUrlRequest2.fullUrl)))
            .andExpect(jsonPath("$[1].shortUrl", `is`(origin + shortenUrlRequest2.customAlias)))

    }

    @Test
    fun shouldRedirectToLongUrl() {

        val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, stubbedAlias)

        shortenUrl(shortenUrlRequest)

        mockMvc.perform(get("/" + shortenUrlRequest.customAlias))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isFound)
            .andExpect(redirectedUrl(shortenUrlRequest.fullUrl))
    }

    @Test
    fun shouldReturnNotFoundWhenShortUrlNotFoundForRedirection() {

        mockMvc.perform(get("/notExists"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound)
            .andExpect(content().string("Alias not found"))
    }

    @Test
    fun shouldDeleteShortURL() {

        val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, stubbedAlias)
        shortenUrl(shortenUrlRequest)

        mockMvc.perform(delete("/" + stubbedAlias))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNoContent)
    }

    @Test
    fun shouldReturnNotFoundWhenShortUrlNotFoundForDeletion() {

        mockMvc.perform(delete("/notExists"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound)
            .andExpect(content().string("Alias not found"))
    }
}