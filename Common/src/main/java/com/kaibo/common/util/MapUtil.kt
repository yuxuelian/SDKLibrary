package com.kaibo.common.util

import java.util.*
import kotlin.collections.Map.Entry

/**
 * @author Administrator
 * @date 2018/4/13 0013 下午 1:32
 * GitHub：
 * email：
 * description：
 */
object MapUtil {

    /**
     * 获取  LinkedHashMap  第一个添加的元素
     * 时间复杂度O(1)
     * @param map
     * @param <K>
     * @param <V>
     * @return
    </V></K> */
    fun <K, V> getHead(map: LinkedHashMap<K, V>): Entry<K, V> {
        return map.entries.iterator().next()
    }

    /**
     * 获取  LinkedHashMap  中最后添加的元素
     * 时间复杂度O(n)
     * @param map
     * @param <K>
     * @param <V>
     * @return
    </V></K> */
    fun <K, V> getTail(map: LinkedHashMap<K, V>): Entry<K, V>? {
        val iterator = map.entries.iterator()
        var tail: Entry<K, V>? = null
        while (iterator.hasNext()) {
            tail = iterator.next()
        }
        return tail
    }

    /**
     * 获取  LinkedHashMap  中最后添加的元素(反射)
     * 时间复杂度O(1)
     * @param map
     * @param <K>
     * @param <V>
     * @return
    </V></K> */
    fun <K, V> getTailByReflection(map: LinkedHashMap<K, V>): Entry<K, V> {
        val tail = map.javaClass.getDeclaredField("tail")
        tail.setAccessible(true)
        @Suppress("UNCHECKED_CAST")
        return tail.get(map) as Entry<K, V>
    }

}
