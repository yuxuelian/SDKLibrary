package com.kaibo.base.util

import java.text.DecimalFormat

/**
 * @author Administrator
 * @date 2018/4/2 0002 下午 3:49
 * GitHub：
 * email：
 * description：
 */

/**
 * 对Double保留两位小数
 */
fun Double.leaveTwoDecimal() = DecimalFormat("#.##").format(this).toDouble()


