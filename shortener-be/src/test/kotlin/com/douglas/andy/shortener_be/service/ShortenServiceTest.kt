package com.douglas.andy.shortener_be.service

import com.douglas.andy.shortener_be.shorten.ShortenService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import kotlin.random.Random
import kotlin.test.assertNotEquals

@SpringBootTest
class ShortenServiceTest {

    @Autowired
    private lateinit var shortenService: ShortenService

    @MockitoBean
    private lateinit var counterService: CounterService

    @Test
    fun shouldInitialiseCounterIncrementEachCallAndyCounterAndReturnACode() {

        val count = Random.nextInt(1, Integer.MAX_VALUE)

        Mockito.`when`(counterService.get()).thenReturn(count)
        Mockito.verify(counterService, Mockito.times(1)).get()

        val result1=shortenService.getEncodedShort()
        val result2=shortenService.getEncodedShort()

        assertNotEquals(result1, result2);
    }
}