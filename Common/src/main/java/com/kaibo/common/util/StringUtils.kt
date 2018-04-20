package com.kaibo.common.util

import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * @author Administrator
 * @date 2018/4/2 0002 上午 11:50
 * GitHub：
 * email：
 * description：
 */

fun String.isNotNull() = this != "" || this.toLowerCase() != "null"


/**
 * 将  String  转成  RequestBody
 */
fun String.toRequestBody(mediaType: MediaType) = RequestBody.create(mediaType, this)