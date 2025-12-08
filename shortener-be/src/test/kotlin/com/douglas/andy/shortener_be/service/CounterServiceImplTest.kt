package com.douglas.andy.shortener_be.service;

import com.douglas.andy.shortener_be.model.Counter
import com.douglas.andy.shortener_be.repository.CounterRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.Optional
import kotlin.random.Random
import kotlin.test.assertEquals


@SpringBootTest
class CounterServiceImplTest {

    @Autowired
    private lateinit var counterService: CounterService

    @MockitoBean
    private lateinit var repository: CounterRepository

    @Test
    fun shouldReturnStartCountIfNoneExistsInRepo() {

        Mockito.`when`(repository.findById(CounterServiceImpl.DOCUMENT_ID)).thenReturn(Optional.empty())

        val actual = counterService.get();

        assertEquals(CounterServiceImpl.START_COUNT, actual);
    }

    @Test
    fun shouldReturnCountFromRepo() {

        val counter = Counter(id = CounterServiceImpl.DOCUMENT_ID, count = 200)

        Mockito.`when`(repository.findById(CounterServiceImpl.DOCUMENT_ID)).thenReturn(Optional.of<Counter>(counter))

        val actual = counterService.get()

        assertEquals(counter.count, actual)
    }

    @Test
    fun shouldSetInRepo() {

        val numToSet = Random.nextInt(1, Integer.MAX_VALUE)

        val counter = Counter(id = 1, count = numToSet)

        Mockito.`when`(repository.save(counter)).thenReturn(counter)

        val result = counterService.set(numToSet)

        assertEquals(numToSet, result)

        Mockito.verify(repository, Mockito.times(1)).save(counter)
    }
}