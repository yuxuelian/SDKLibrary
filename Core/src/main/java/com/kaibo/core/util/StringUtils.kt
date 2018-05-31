package com.kaibo.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * @author Administrator
 * @date 2018/4/2 0002 上午 11:50
 * GitHub：
 * email：
 * description：
 */

/**
 * 判断当前字符串是否是空   如果即不等于  ""  也不等于  "null"  那么认为他不是null
 */
fun String.isNotEmpty() = this != "" && this.toLowerCase() != "null"


/**
 * 将  String  转成  RequestBody
 */
fun String.toRequestBody(mediaType: MediaType) = RequestBody.create(mediaType, this)
