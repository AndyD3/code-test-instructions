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

    override fun get(): Int {
        val foundItem=repository.findById(DOCUMENT_ID);
        if(foundItem.isPresent())
            return foundItem.get().count
        else
            return START_COUNT;
    }

    override fun set(count: Int): Int {
        return repository.save(Counter(id=DOCUMENT_ID,count=count)).count
    }
}