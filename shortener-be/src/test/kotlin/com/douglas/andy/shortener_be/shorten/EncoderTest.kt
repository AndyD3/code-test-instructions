package com.douglas.andy.shortener_be.shorten;

import com.mongodb.assertions.Assertions.assertTrue
import org.junit.jupiter.api.Test;

import kotlin.random.Random

import org.assertj.core.api.Assertions.assertThat;

class EncoderTest {

    private var encoder=Encoder();

    @Test
    fun shouldGenerateSameOutputForSameCount() {

        val random=Random.nextInt(1, Integer.MAX_VALUE)

        val result1 =encoder.encode(random)
        val result2 =encoder.encode(random)

        assertThat(result1).isEqualTo(result2)
    }

    @Test
    fun shouldGenerateDifferentOutputEachCall() {

        val set=HashSet<String>();
        val numbersToTry=300;

        for(i in 1..numbersToTry) {
            assertTrue( set.add(encoder.encode(i)))
        }
    }

}
