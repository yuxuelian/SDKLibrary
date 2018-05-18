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


    @Test
    fun test1() {
        val str1 = ""
        val str2 = ""
        val str3 = ""
        val str4 = ""

        val arrayList = ArrayList<String>()

        arrayList.add(str1)
        arrayList.add(str2)
        arrayList.add(str3)
        arrayList.add(str4)

        val str5 = ""
        val str6 = ""
        val str7 = ""
        val str8 = ""

        val arrayList2 = ArrayList<String>()

        arrayList2.add(str5)
        arrayList2.add(str6)
        arrayList2.add(str7)
        arrayList2.add(str8)

        arrayList2.forEachIndexed { index, s ->
            arrayList[index] = s
        }

    }

    class Aaa() {
        init {
            println("执行构造")
        }

        fun test() {
            println("测试----")
        }
    }

    @Test
    fun test3() {
        println(String.format("%02x", 20 and 0xFF))
    }

}
