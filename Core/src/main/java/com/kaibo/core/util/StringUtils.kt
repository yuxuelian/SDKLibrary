package com.kaibo.core.util

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * @author:Administrator
 * @date:2018/4/2 0002 上午 11:50
 * GitHub:
 * email:
 * description:
 */

/**
 * 判断当前字符串是否是空   如果即不等于  ""  也不等于  "null"  那么认为他不是null
 */
fun String.isNotEmpty() = this != "" && this.toLowerCase() != "null"

/**
 * 将  String  转成  RequestBody
 */
fun String.toRequestBody(mediaType: MediaType) = RequestBody.create(mediaType, this)

/**
 * 将路径列表转换成  List<MultipartBody.Part>
 *  适用于后台一个key接收文件数组的情况
 */
fun List<String>.toMultiBodyParts(key: String, mediaType: MediaType?) = this
        .filter { it.isNotEmpty() }
        .map {
            val file = File(it)
            MultipartBody.Part.createFormData(key, file.name, RequestBody.create(mediaType, file))
        }

fun String.toFile() = File(this)