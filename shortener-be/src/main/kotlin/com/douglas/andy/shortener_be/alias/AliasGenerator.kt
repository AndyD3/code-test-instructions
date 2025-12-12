package com.douglas.andy.shortener_be.alias

import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class AliasGenerator (val length:Int=7){

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