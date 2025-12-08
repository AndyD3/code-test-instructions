package com.douglas.andy.shortener_be.shorten

class UrlEncoder{

    companion object {
        const val CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val BASE = CHARACTERS.length
    }

    // effectively converts a decimal (each position has 10 possible digits) to a string where each position has 62 possible digits
    // this means less space occupied and there is a unique code for each number supplied as input
    fun encode(counter:Int): String {

        var number=counter;

        val stringBuilder = StringBuilder()

        while (number > 0) {
            stringBuilder.append(CHARACTERS[number % BASE])
            number /= BASE
        }

        return stringBuilder.reverse().toString()
    }
}
