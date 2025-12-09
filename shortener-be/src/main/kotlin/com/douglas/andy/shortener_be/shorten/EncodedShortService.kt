package com.douglas.andy.shortener_be.shorten

import com.douglas.andy.shortener_be.service.CounterService
import org.springframework.stereotype.Service

@Service
class EncodedShortService (var counterService: CounterService, var encoder: Encoder) {

    fun getEncodedShort(): String {
        val count = counterService.incrementAndGet()
        return encoder.encode(count)
    }
}