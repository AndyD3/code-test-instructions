package com.douglas.andy.shortener_be.controller

import com.douglas.andy.shortener_be.model.ShortenedUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.random.Random

@RestController
@CrossOrigin(origins = ["http://localhost:5173"])
@RequestMapping("/shortener")
class ShortenerController {

    @GetMapping("/urls")
    fun list(): ResponseEntity<List<ShortenedUrl>> {
        val urls = listOf(ShortenedUrl("alias1", ""+Random.nextInt(), "shortUrl1"), ShortenedUrl("alias1", ""+Random.nextInt(), "shortUrl1"));
        return ResponseEntity.ok(urls)
    }
}