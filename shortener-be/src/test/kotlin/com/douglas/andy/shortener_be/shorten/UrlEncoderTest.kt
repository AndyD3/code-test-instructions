package com.douglas.andy.shortener_be.shorten;

import org.junit.jupiter.api.Test;

import kotlin.random.Random

import org.assertj.core.api.Assertions.assertThat;

class UrlEncoderTest {

    private var encoder=UrlEncoder();

    @Test
    fun shouldGenerateSameOutputForSameCount() {

        val random=Random.nextInt(1, Integer.MAX_VALUE)

        val result1 =encoder.encode(random)
        val result2 =encoder.encode(random)

        assertThat(result1).isEqualTo(result2)
    }

    @Test
    fun shouldGenerateDifferentOutputEachCall() {

        val result1=encoder.encode(1)
        val result2=encoder.encode(2)
        val result3=encoder.encode(3)

        assertThat(result1).isNotEqualTo(result2)
        assertThat(result1).isNotEqualTo(result3)
        assertThat(result2).isNotEqualTo(result3)
    }
}
