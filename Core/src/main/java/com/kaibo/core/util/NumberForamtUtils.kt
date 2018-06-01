package com.kaibo.core.util

/**
 * @author:Administrator
 * @date:2018/4/2 0002 下午 3:49
 * GitHub:
 * email:
 * description:
 */

/**
 * 对Double保留两位小数
 */
fun Double.leaveTwoDecimal(): Double = Math.round(this * 100) / 100.0


