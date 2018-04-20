package com.kaibo.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate


/**
 * @author Administrator
 * @date 2018/4/20 0020 下午 2:42
 * GitHub：
 * email：
 * description：
 */

/**
 * 根据手机分辨率从DP转成PX
 * @param dpValue
 * @return
 */
fun Context.dp2px(dpValue: Float) = dpValue * resources.displayMetrics.density

/**
 * 将sp值转换为px值，保证文字大小不变
 * @param spValue
 * @return
 */
fun Context.sp2px(spValue: Float) = spValue * resources.displayMetrics.scaledDensity

/**
 * 根据手机的分辨率PX(像素)转成DP
 * @param pxValue
 * @return
 */
fun Context.px2dp(pxValue: Float) = pxValue / resources.displayMetrics.density

/**
 * 将px值转换为sp值，保证文字大小不变
 * @param pxValue
 * @return
 */
fun Context.px2sp(pxValue: Float) = pxValue / resources.displayMetrics.scaledDensity

/**
 * 获取当前APP的版本号
 */
val Context.versionCode get() = packageManager.getPackageInfo(packageName, 0).versionCode

/**
 * 获取当前APP的版本名
 */
val Context.versionName: String get() = packageManager.getPackageInfo(packageName, 0).versionName

/**
 * 判断是否开启gps定位.
 *
 * @return 如果开启gps定位返回true, 否则返回false
 */
val Context.isGPSEnable get() = (getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)

/**
 * 判断是否有网络连接.
 *
 * @return if the network is available, `false` otherwise
 */
val Context.isNetworkConnected get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo.isAvailable

/**
 * 检测是否有存在外部存储.
 *
 * @return 如果存在返回true, 否则返回false
 */
fun hasExternalStorage() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

/**
 * 跳转到该app对应的设置界面.
 */
fun Context.toAppSetting() {
    this.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")))
}

/**
 * 检测是否为模拟器.
 *
 * @return true表示模拟器
 */
fun isEmulator(): Boolean {
    val knownQemuDrivers = arrayOf("goldfish")
    val driverFile = File("/proc/tty/drivers")
    if (driverFile.exists() && driverFile.canRead()) {
        val data = ByteArray(driverFile.length().toInt())
        val inStream = FileInputStream(driverFile)
        inStream.read(data)
        inStream.close()
        val driverData = String(data)
        for (known_qemu_driver in knownQemuDrivers) {
            if (driverData.contains(known_qemu_driver)) {
                return true    //基于qemu模拟器
            }
        }
    }
    return false
}

/**
 * 判断当前手机是否有ROOT权限
 *
 * @return
 */
fun isRoot() = !(!File("/system/bin/su").exists() && !File("/system/xbin/su").exists())

/**
 * 判断当前手机是否有ROOT权限
 *
 * @return
 */
val Context.sha1
    get() = {
        //获取包管理器
        //获取当前要获取SHA1值的包名，也可以用其他的包名，但需要注意，
        //在用其他包名的前提是，此方法传递的参数Context应该是对应包的上下文。
        //返回包括在包中的签名信息
        //签名信息
        //将签名转换为字节数组流
        @SuppressLint("PackageManagerGetSignatures")
        val cert = this.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures[0].toByteArray()

        //证书工厂类，这个类实现了出厂合格证算法的功能
        //X509证书，X.509是一种非常通用的证书格式
        val x509Certificate = CertificateFactory
                .getInstance("X509")
                .generateCertificate(ByteArrayInputStream(cert)) as X509Certificate

        //加密算法的类，这里的参数可以是MD4,MD5等加密算法
        //获得公钥
        val publicKey = MessageDigest.getInstance("SHA1").digest(x509Certificate.encoded)

        //字节到十六进制的格式转换
        byte2HexFormatted(publicKey)
    }.invoke()


/**
 * 给app设置一个通用字体.一般在Application的onCreate方法中调用
 *
 * @param context                 context
 * @param staticTypefaceFieldName 被替换掉的系统字体类型
 * @param fontName           用于替换的字体文件名(文件放在assets目录下)
 */
fun Context.setFont(staticTypefaceFieldName: String, fontName: String) {
    val regular = Typeface.createFromAsset(assets, fontName)
    replaceFont(staticTypefaceFieldName, regular)
}

/**
 * 用newTypeface替换掉staticTypefaceFieldName.
 *
 * @param staticTypefaceFieldName 被替换掉的系统字体类型
 * ----- SERIF -----
 * ----- MONOSPACE -----
 * ----- SANS -----
 * ----- NORMAL -----
 * @param newTypeface             用于替换的字体文件
 */
private fun replaceFont(staticTypefaceFieldName: String, newTypeface: Typeface) {
    Typeface::class.java.getDeclaredField(staticTypefaceFieldName)
            .apply {
                isAccessible = true
                set(null, newTypeface)
            }
}

//这里是将获取到得编码进行16进制转换
private fun byte2HexFormatted(arr: ByteArray): String {
    val str = StringBuilder(arr.size * 2)
    arr.forEachIndexed { index, it ->
        var h = Integer.toHexString(it.toInt())
        val l = h.length

        if (l == 1) {
            h = "0$h"
        } else if (l > 2) {
            h = h.substring(l - 2, l)
        }

        str.append(h.toUpperCase())
        if (index != arr.size - 1) {
            str.append(':')
        }
    }
    return str.toString()
}









