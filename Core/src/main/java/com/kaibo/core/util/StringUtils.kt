package com.kaibo.core.util

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.security.MessageDigest

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
fun String.toRequestBody(mediaType: MediaType): RequestBody = RequestBody.create(mediaType, this)

/**
 * 将路径列表转换成  List<MultipartBody.Part>
 *  适用于后台一个key接收文件数组的情况
 */
fun List<String>.toMultiBodyParts(key: String, mediaType: MediaType?): List<MultipartBody.Part> = this
        .filter {
            it.isNotEmpty()
        }
        .map {
            val file = it.toFile()
            MultipartBody.Part.createFormData(key, file.name, RequestBody.create(mediaType, file))
        }

fun String.toFile() = File(this)

fun String.toMd5(): String {
    val md5: MessageDigest = MessageDigest.getInstance("MD5")
    md5.update(this.toByteArray())
    val encryption: ByteArray = md5.digest()
    val strBuf = StringBuffer()

    encryption.forEach {
        val enc = it.toInt()
        if (Integer.toHexString(0xFF and enc).length == 1) {
            strBuf.append("0").append(Integer.toHexString(0xFF and enc))
        } else {
            strBuf.append(Integer.toHexString(0xFF and enc))
        }
    }

    return strBuf.toString()
}