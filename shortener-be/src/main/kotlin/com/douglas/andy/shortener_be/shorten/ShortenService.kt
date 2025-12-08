package com.douglas.andy.shortener_be.shorten

import com.douglas.andy.shortener_be.service.CounterService
import org.springframework.stereotype.Service

@Service
class ShortenService (var counterService: CounterService) {

    //TODO - how about an atomic update function rather than get and set??

    private var count : Int = counterService.get();

    private val urlEncoder = UrlEncoder();

    fun getEncodedShort(): String {
        count++;
        counterService.set(count);
        return urlEncoder.encode(count)
    }
}