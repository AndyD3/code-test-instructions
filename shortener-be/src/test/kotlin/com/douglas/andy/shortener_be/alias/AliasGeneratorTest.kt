package com.douglas.andy.shortener_be.alias

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertNotEquals

class AliasGeneratorTest {

    @Test
    fun shouldGenerateCodeOfSpecifiedLength() {

        assertEquals(1, AliasGenerator(1).generate().length)
        assertEquals(3, AliasGenerator(3).generate().length)
        assertEquals(5, AliasGenerator(5).generate().length)
        assertEquals(7, AliasGenerator(7).generate().length)
        assertEquals(10, AliasGenerator(10).generate().length)
    }

    @Test
    fun shouldGenerateDifferentAliasEachCall() {

        // this is acceptable but there's a tiny probability of it failing as random numbers hit each other
        val aliasGenerator= AliasGenerator(10)

        val call1=aliasGenerator.generate()
        val call2=aliasGenerator.generate()
        val call3=aliasGenerator.generate()

        assertNotEquals(call1, call2);
        assertNotEquals(call1, call3);
        assertNotEquals(call2, call3);
    }
}