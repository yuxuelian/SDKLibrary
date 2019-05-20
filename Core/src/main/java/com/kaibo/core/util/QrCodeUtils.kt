package com.kaibo.core.util

import android.content.Context
import android.graphics.*
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.kaibo.core.R
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author kaibo
 * @date 2018/6/26 17:51
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：二维码相关操作
 */

suspend fun Context.createQrCodeAsync(qrCode: String,
                                      qrCodeSize: Int,
                                      @ColorInt borderColor: Int = ContextCompat.getColor(this, R.color.color_d0d0d0)) =
        suspendCoroutine<Bitmap> { continuation ->
            // 子线程执行这个任务
            executorService.submit {
                try {
                    val res = createQRImage(qrCode, qrCodeSize).addBorder((qrCodeSize * .04f).toInt(), borderColor)
                    continuation.resume(res)
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }
        }

/**
 * 生成二维码Bitmap
 *
 * @param content   内容
 * @param size  图片大小
 * @param logoBm    二维码中心的Logo图标（可以为null）
 * @return 生成二维码及保存文件是否成功
 * 这个方法是耗时操作    建议在子线程执行
 */
fun createQRImage(content: String, size: Int, logoBm: Bitmap? = null): Bitmap {
    // 配置参数
    val hints: MutableMap<EncodeHintType, Any> = HashMap()
    hints[EncodeHintType.CHARACTER_SET] = "utf-8"
    // 容错级别
    hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
    // 上下左右的空白边距
    hints[EncodeHintType.MARGIN] = 0
    // 图像数据转换，使用了矩阵转换
    val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
    val pixels = IntArray(size * size)
    // 下面这里按照二维码的算法，逐个生成二维码的图片，
    // 两个for循环是图片横列扫描的结果
    for (y in 0 until size) {
        for (x in 0 until size) {
            if (bitMatrix.get(x, y)) {
                // 黑色
                pixels[y * size + x] = -0x1000000
            } else {
                // 透明
                pixels[y * size + x] = 0
            }
        }
    }
    // 生成二维码图片的格式，使用ARGB_8888
    var bitmap: Bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
    if (logoBm != null) {
        // 处理一下logo   添加一些阴影
        bitmap = addLogo(bitmap, logoBm.dispose())
    }
    return bitmap
}

private fun Bitmap.dispose(): Bitmap {
    val width = this.width
    val height = this.height
    val borderLeft = width * 0.1f
    val borderTop = height * 0.1f
    val resBitmap = Bitmap.createBitmap((
            width + borderLeft * 2).toInt(),
            (height + borderTop * 2).toInt(),
            Bitmap.Config.ARGB_8888)
    val canvas = Canvas(resBitmap)

    val paint = Paint()
    paint.strokeWidth = 2f
    // 开启抗锯齿
    paint.isAntiAlias = true

    // 绘制带阴影的圆角矩形
    paint.style = Paint.Style.FILL
    paint.color = Color.WHITE
    paint.setShadowLayer(10f, 2f, 2f, Color.BLACK)
    val rectF1 = RectF(
            borderLeft / 2,
            borderTop / 2,
            resBitmap.width - borderLeft / 2,
            resBitmap.height - borderTop / 2)
    canvas.drawRoundRect(rectF1, borderLeft * 2, borderTop * 2, paint)
    // 绘制logo
    canvas.drawBitmap(this, borderLeft, borderTop, null)
    // 绘制空心的圆角矩形
    paint.style = Paint.Style.STROKE
    paint.color = Color.GRAY
    paint.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
    val rectF2 = RectF(
            borderLeft,
            borderTop,
            resBitmap.width - borderLeft,
            resBitmap.height - borderTop)
    canvas.drawRoundRect(rectF2, borderLeft * 2, borderTop * 2, paint)

    return resBitmap
}

/**
 * 在二维码中间添加Logo图案
 */
private fun addLogo(src: Bitmap, logo: Bitmap): Bitmap {
    //获取图片的宽高
    val srcWidth = src.width
    val srcHeight = src.height
    val logoWidth = logo.width
    val logoHeight = logo.height
    //logo大小为二维码整体大小的1/5
    val scaleFactor = srcWidth / (5f * logoWidth)
    val bitmap: Bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawBitmap(src, 0f, 0f, null)
    canvas.scale(scaleFactor, scaleFactor, srcWidth / 2f, srcHeight / 2f)
    canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2f, (srcHeight - logoHeight) / 2f, null)
    return bitmap
}

