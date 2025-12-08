package com.douglas.andy.shortener_be.shorten

import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger

@Service
class ShortenService {

    @Volatile
    private var count = AtomicInteger(1);

    val urlEncoder = UrlEncoder();

    fun getEncodedShort(): String {
        return urlEncoder.encode(count.getAndIncrement())
    }
}