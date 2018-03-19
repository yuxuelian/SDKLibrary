package com.kaibo.demo

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    private val aaa = Aaa()

    @Test
    fun testByLazy() {
        println("testByLazy1")
        aaa.test()
        println("testByLazy2")
        aaa.test()
        aaa.test()
        aaa.test()
    }


    class Aaa() {
        init {
            println("执行构造")
        }

        fun test() {
            println("测试----")
        }
    }

}
