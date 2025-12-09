package com.douglas.andy.shortener_be.service

import com.douglas.andy.shortener_be.model.Counter
import com.douglas.andy.shortener_be.repository.CounterRepository
import org.springframework.stereotype.Service

@Service
class CounterServiceImpl(private val repository: CounterRepository) : CounterService {

    companion object {
        const val DOCUMENT_ID = 1;
        const val START_COUNT = 1;
    }

    override fun incrementAndGet(): Int {
        val foundItem=repository.findById(DOCUMENT_ID);

        val count=if(foundItem.isPresent())
            foundItem.get().count+1
        else
            START_COUNT;

        return repository.save(Counter(id=DOCUMENT_ID,count=count)).count
    }
}