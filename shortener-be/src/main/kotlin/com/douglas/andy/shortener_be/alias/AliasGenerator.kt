package com.douglas.andy.shortener_be.alias

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class AliasGenerator (@Value("\${shortener.alias.length}") val length: Int){

  companion object {
      const val CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
      const val BASE = CHARACTERS.length
  }

  val random =SecureRandom();

  fun generate():String {
    val stringBuilder = StringBuilder(length)

    repeat(length) {
      stringBuilder.append(CHARACTERS[random.nextInt(BASE)]);
    }
    return stringBuilder.toString()
  }
}