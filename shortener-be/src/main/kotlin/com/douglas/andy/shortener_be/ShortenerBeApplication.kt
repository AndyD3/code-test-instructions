package com.douglas.andy.shortener_be

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShortenerBeApplication

fun main(args: Array<String>) {
	runApplication<ShortenerBeApplication>(*args)
}
