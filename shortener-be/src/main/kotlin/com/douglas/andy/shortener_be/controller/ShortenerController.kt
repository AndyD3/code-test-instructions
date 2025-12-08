package com.douglas.andy.shortener_be.controller

import com.douglas.andy.shortener_be.exception.ShortURLExistsException
import com.douglas.andy.shortener_be.model.ShortenUrlRequest
import com.douglas.andy.shortener_be.model.ShortenUrlResponse
import com.douglas.andy.shortener_be.model.ShortenedUrl
import com.douglas.andy.shortener_be.service.ShortenedUrlService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:5173"])
@RequestMapping("/")
class ShortenerController(private val service: ShortenedUrlService) {

    @ExceptionHandler
    fun handleNoSuchElementException(ex: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity("Alias not found", HttpStatus.NOT_FOUND)

    @ExceptionHandler
    fun handleShortURLExistsException(ex: ShortURLExistsException): ResponseEntity<String> =
        ResponseEntity("Invalid input or alias already taken", HttpStatus.BAD_REQUEST)


    @GetMapping("/{shortURL}")
    fun redirectToFullUrl(response: HttpServletResponse, @PathVariable shortURL: String) =
        response.sendRedirect(service.findById(shortURL).fullUrl)


    @GetMapping("/urls")
    fun list(): ResponseEntity<List<ShortenedUrl>> = ResponseEntity.ok(service.findAll())


    @PostMapping("/shorten")
    fun create(@RequestBody request: ShortenUrlRequest): ResponseEntity<ShortenUrlResponse> {

        //TODO check for empty long URL

        // simplest path for now to prove connectivity across FE-BE and DB
        if (!request.customAlias.isNullOrEmpty()) {

            //TODO tidy
            val shortened = ShortenedUrl(request.fullUrl, request.customAlias);
            val returned = service.create(shortened)
            val response = ShortenUrlResponse(returned.shortUrl)

            return ResponseEntity(response, HttpStatus.CREATED);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("encoded URL not supported yet") as ResponseEntity<ShortenUrlResponse>
    }
}