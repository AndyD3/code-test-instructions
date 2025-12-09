package com.douglas.andy.shortener_be.service;

import com.douglas.andy.shortener_be.model.Counter
import com.douglas.andy.shortener_be.repository.CounterRepository
import com.douglas.andy.shortener_be.service.CounterServiceImpl.Companion.DOCUMENT_ID
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
    fun shouldReturnStartCountIfNoneExistsInRepoThenAddIncrementedToRepo() {

        val counter=Counter(id=DOCUMENT_ID,count=CounterServiceImpl.START_COUNT)

        Mockito.`when`(repository.findById(CounterServiceImpl.DOCUMENT_ID)).thenReturn(Optional.empty())
        Mockito.`when`(repository.save(counter)).thenReturn(counter)

        val actual = counterService.incrementAndGet();

        assertEquals(CounterServiceImpl.START_COUNT, actual);

        Mockito.verify(repository, Mockito.times(1)).save(counter)
    }

    @Test
    fun shouldReturnCountPlusOneThenAddIncrementedToRepo() {

        val preNumber=Random.nextInt(1, Integer.MAX_VALUE);

        val counter=Counter(id=DOCUMENT_ID,count=preNumber)
        val counter2=Counter(id=DOCUMENT_ID,count=preNumber+1)

        Mockito.`when`(repository.findById(CounterServiceImpl.DOCUMENT_ID)).thenReturn(Optional.of(counter))
        Mockito.`when`(repository.save(counter2)).thenReturn(counter2)

        val actual = counterService.incrementAndGet();

        assertEquals(counter2.count, actual);

        Mockito.verify(repository, Mockito.times(1)).save(counter2)
    }

}