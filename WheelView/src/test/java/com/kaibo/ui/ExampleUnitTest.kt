package com.kaibo.ui

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Test
    fun test1() {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        println(simpleDateFormat.parse("2018-12-31 23:55:55").time)
    }
}