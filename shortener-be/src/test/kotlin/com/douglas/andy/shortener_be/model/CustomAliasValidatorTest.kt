package com.douglas.andy.shortener_be.model

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.random.Random

@SpringBootTest
internal class CustomAliasValidatorTest {
  @Autowired
  private val customAliasValidator: CustomAliasValidator? = null

  private val validator: Validator = Validation.buildDefaultValidatorFactory().getValidator()

  val stubbedUrlL = "https://en.wikipedia.org/wiki/Doge_(meme)" + Random.nextInt()
  val stubbedAlias = "stubbedAlias" + Random.nextInt()

  @Test
  fun shouldReturnValidWhenAllFieldsValid() {
    val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, stubbedAlias)

    val constraintViolations =
      validator.validate(shortenUrlRequest)
    Assertions.assertEquals(0, constraintViolations.size)
  }

  @Test
  fun shouldReturnValidWhenURLGoodAndAliasBlank() {
    val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, "")

    val constraintViolations =
      validator.validate<ShortenUrlRequest?>(shortenUrlRequest)
    Assertions.assertEquals(0, constraintViolations.size)
  }

  @Test
  fun shouldReturnInvalidForInvalidUrl() {
    val shortenUrlRequest = ShortenUrlRequest("notValidUrl", stubbedAlias)

    val constraintViolations =
      validator.validate(shortenUrlRequest)
    Assertions.assertEquals(1, constraintViolations.size)
    Assertions.assertEquals(
      "must be a valid URL",
      constraintViolations.iterator().next()!!.getMessage()
    )
  }

  @Test
  fun shouldReturnInvalidWhenNoUrl() {
    val shortenUrlRequest = ShortenUrlRequest("", stubbedAlias)

    val constraintViolations =
      validator.validate(shortenUrlRequest)
    Assertions.assertEquals(1, constraintViolations.size)
    Assertions.assertEquals(
      "must not be blank",
      constraintViolations.iterator().next()!!.getMessage()
    )
  }

  @Test
  fun shouldReturnInvalidWhenCustomAlianTooShort() {
    val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, "12345")

    val constraintViolations =
      validator.validate(shortenUrlRequest)
    Assertions.assertEquals(1, constraintViolations.size)
    Assertions.assertEquals(
      "Invalid value for custom alias",
      constraintViolations.iterator().next()!!.getMessage()
    )
  }

  @Test
  fun shouldReturnInvalidWhenCustomAliasTooLong() {
    val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, "1234567890123456789012345678901")

    val constraintViolations =
      validator.validate(shortenUrlRequest)
    Assertions.assertEquals(1, constraintViolations.size)
    Assertions.assertEquals(
      "Invalid value for custom alias",
      constraintViolations.iterator().next()!!.getMessage()
    )
  }

  @Test
  fun shouldReturnInvalidWhenCustomAliasHasInvalidCharacters() {
    val shortenUrlRequest = ShortenUrlRequest(stubbedUrlL, "%"+stubbedAlias )

    val constraintViolations =
      validator.validate(shortenUrlRequest)
    Assertions.assertEquals(1, constraintViolations.size)
    Assertions.assertEquals(
      "Invalid value for custom alias",
      constraintViolations.iterator().next()!!.getMessage()
    )
  }

}