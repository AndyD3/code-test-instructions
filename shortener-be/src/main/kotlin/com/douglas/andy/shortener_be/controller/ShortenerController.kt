package com.douglas.andy.shortener_be.controller

import com.douglas.andy.shortener_be.alias.AliasCreator
import com.douglas.andy.shortener_be.exception.ShortURLExistsException
import com.douglas.andy.shortener_be.model.ShortenUrlRequest
import com.douglas.andy.shortener_be.model.ShortenUrlResponse
import com.douglas.andy.shortener_be.model.ShortenedUrlDAO
import com.douglas.andy.shortener_be.service.ShortenedUrlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:5173"])
@RequestMapping("/")
class ShortenerController(
    private val service: ShortenedUrlService,
    private val aliasCreator: AliasCreator
) {
    var logger: Logger = LoggerFactory.getLogger(AliasCreator::class.java)

    @ExceptionHandler
    fun handleNoSuchElementException(ex: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity("Alias not found", HttpStatus.NOT_FOUND)

    @ExceptionHandler
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<String> {
        logger.warn("Validation error: ${ex.bindingResult.allErrors}")
        return ResponseEntity("Invalid input or alias already taken", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleShortURLExistsException(ex: ShortURLExistsException): ResponseEntity<String> =
        ResponseEntity("Invalid input or alias already taken", HttpStatus.BAD_REQUEST)

    @GetMapping("/{shortURL}")
    fun redirectToFullUrl(response: HttpServletResponse, @PathVariable shortURL: String) =
        response.sendRedirect(service.findById(shortURL).fullUrl)


    @GetMapping("/urls")
    fun list(): ResponseEntity<List<ShortenedUrlDAO>> = ResponseEntity.ok(service.findAll())

    @PostMapping("/shorten")
    fun create(@RequestBody @Valid request: ShortenUrlRequest): ResponseEntity<ShortenUrlResponse> {
        val created = aliasCreator.create(request)
        return ResponseEntity(ShortenUrlResponse(created.alias), HttpStatus.CREATED);
    }

    @DeleteMapping("/{shortURL}")
    fun delete(response: HttpServletResponse, @PathVariable shortURL: String): ResponseEntity<String> {
        service.deleteById(shortURL);
        return ResponseEntity.noContent().build();
    }
}