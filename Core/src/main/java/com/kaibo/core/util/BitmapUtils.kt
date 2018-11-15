package com.kaibo.core.util

import android.graphics.*
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * @author kaibo
 * @date 2018/8/7 19:34
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

fun Bitmap.toBase64(): String {
    // 要返回的字符串
    ByteArrayOutputStream().use {
        this.compress(Bitmap.CompressFormat.PNG, 100, it)
        // 转换为字符串
        return Base64.encodeToString(it.toByteArray(), Base64.DEFAULT)
    }
}

/**
 * 将 Bitmap 保存到指定的文件
 * @param file 保存到指定的文件
 */
fun Bitmap.saveToFile(file: File) {
    if (file.exists()) {
        file.delete()
    }
    file.createNewFile()
    val out = FileOutputStream(file)
    this.compress(Bitmap.CompressFormat.PNG, 90, out)
    out.flush()
    out.close()
}

/**
 * 从磁盘上获取Bitmap
 *
 * @param path
 * @param name
 * @return
 */
fun File.toBitmap(): Bitmap = BitmapFactory.decodeStream(FileInputStream(this))

/**
 * 指定尺寸缩放bitmap
 *
 * @param bitmap
 * @param w
 * @param h
 * @return
 */
fun Bitmap.resize(w: Int, h: Int): Bitmap {
    val width = this.width
    val height = this.height

    val scaleWidth = w.toFloat() / width
    val scaleHeight = h.toFloat() / height

    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.decorate(size: Float): Bitmap {
    val resBitmap = Bitmap.createBitmap(size.toInt(), size.toInt(), Bitmap.Config.ARGB_4444)
    val canvas = Canvas(resBitmap)
    // 画中间的二维码
    val qrCodeSize = (400 / 515.0 * size).toInt()
    val bitmapLeft = (57 / 515.0 * size).toFloat()
    val bitmapTop = (57 / 515.0 * size).toFloat()
    canvas.drawBitmap(this.resize(qrCodeSize, qrCodeSize), bitmapLeft, bitmapTop, null)

    // 线宽
    val lineWidth = (10 / 515.0 * size).toFloat()
    val paint = Paint()
    paint.strokeWidth = lineWidth
    paint.style = Paint.Style.FILL

    val leftRightWidth = (50 / 515.0 * size).toFloat()

    // 画左上角第一个矩形
    paint.color = Color.parseColor("#D00000")
    canvas.drawRect(RectF(0f, 0f, leftRightWidth, size / 2), paint)

    // 画顶部矩形
    paint.color = Color.parseColor("#5F3FAB")
    canvas.drawRect(RectF(size / 2, 0f, size, leftRightWidth), paint)

    // 画右侧矩形
    paint.color = Color.parseColor("#ECCC1F")
    canvas.drawRect(RectF(size - leftRightWidth, size / 2, size, size), paint)

    // 画左侧底部矩形
    paint.color = Color.parseColor("#008FEB")
    canvas.drawRect(RectF(0f, size - leftRightWidth, size / 2, size), paint)

    paint.color = Color.BLACK
    canvas.drawLine(leftRightWidth, leftRightWidth, size, leftRightWidth, paint)
    canvas.drawLine(size - leftRightWidth, leftRightWidth, size - leftRightWidth, size, paint)
    canvas.drawLine(size - leftRightWidth, size - leftRightWidth, 0f, size - leftRightWidth, paint)
    canvas.drawLine(leftRightWidth, size - leftRightWidth, leftRightWidth, 0f, paint)
    return resBitmap
}

