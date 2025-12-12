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
import com.douglas.andy.shortener_be.alias.AliasGenerator

@RestController
@CrossOrigin(origins = ["http://localhost:5173"])
@RequestMapping("/")
class ShortenerController(
    private val service: ShortenedUrlService,
    private val aliasGenerator: AliasGenerator
) {

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

        // TODO (if I have time spare) check for empty long URL - returns bad request if body bad but would be better to have message "Invalid input or alias already taken"
        // TODO the short URL generated could be duplicate of an alias created shortened URL

        //can this call a different method in the the service based on the type of shortenurlrequest?


        val shortened =
            if (!request.customAlias.isNullOrEmpty())
                ShortenedUrl(request.fullUrl, request.customAlias);
            else
                ShortenedUrl(request.fullUrl, aliasGenerator.generate());

        val created = service.create(shortened)
        return ResponseEntity(ShortenUrlResponse(created.shortUrl), HttpStatus.CREATED);
    }

    @DeleteMapping("/{shortURL}")
    fun delete(response: HttpServletResponse, @PathVariable shortURL: String): ResponseEntity<String> {
        service.deleteById(shortURL);
        return ResponseEntity.noContent().build();
    }
}