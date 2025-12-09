package com.douglas.andy.shortener_be.shorten

import com.douglas.andy.shortener_be.service.CounterService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import kotlin.random.Random

@SpringBootTest
class EncodedShortServiceTest {

    @Autowired
    private lateinit var encodedShortService: EncodedShortService

    @MockitoBean
    private lateinit var counterService: CounterService

    @MockitoBean
    private lateinit var encoder: Encoder;

    @Test
    fun shouldGetAndIncrementCountThenFetchEncodingForThatCount() {

        val count = Random.nextInt(1, Integer.MAX_VALUE)

        Mockito.`when`(counterService.incrementAndGet()).thenReturn(count)

        encodedShortService.getEncodedShort()

        Mockito.verify(encoder, Mockito.times(1)).encode(count);
        Mockito.verify(counterService, Mockito.times(1)).incrementAndGet()
    }
}