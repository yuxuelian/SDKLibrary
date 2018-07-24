package com.kaibo.core.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.LocaleList
import android.provider.Settings
import android.support.v4.content.FileProvider
import androidx.core.net.toUri
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*


/**
 * @author:Administrator
 * @date:2018/4/20 0020 下午 2:42
 * GitHub:
 * email:
 * description:
 */

///**
// * 根据手机分辨率从DP转成PX
// * @param dpValue
// * @return
// */
//fun Context.dp2px(dpValue: Float) = dpValue * resources.displayMetrics.density
//
///**
// * 将sp值转换为px值，保证文字大小不变
// * @param spValue
// * @return
// */
//fun Context.sp2px(spValue: Float) = spValue * resources.displayMetrics.scaledDensity
//
///**
// * 根据手机的分辨率PX(像素)转成DP
// * @param pxValue
// * @return
// */
//fun Context.px2dp(pxValue: Float) = pxValue / resources.displayMetrics.density
//
///**
// * 将px值转换为sp值，保证文字大小不变
// * @param pxValue
// * @return
// */
//fun Context.px2sp(pxValue: Float) = pxValue / resources.displayMetrics.scaledDensity

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
    val driverFile = File("/proc/tty/drivers")
    if (driverFile.exists() && driverFile.canRead()) {
        val data = ByteArray(driverFile.length().toInt())
        val inStream = FileInputStream(driverFile)
        inStream.read(data)
        inStream.close()
        val driverData = String(data)
        for (known_qemu_driver in arrayOf("goldfish")) {
            if (driverData.contains(known_qemu_driver)) {
                return true
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
 * 获取当前APP的SHA1签名信息
 * @sign SHA1  MD5
 * @return
 */
fun Context.sign(sign: String = "SHA1"): String {
    @SuppressLint("PackageManagerGetSignatures")
    val cert: ByteArray = this.packageManager
            .getPackageInfo(this.packageName, PackageManager.GET_SIGNATURES)
            .signatures[0]
            .toByteArray()

    //X509证书，X.509是一种非常通用的证书格式
    val x509Certificate = (CertificateFactory
            .getInstance("X509")
            .generateCertificate(ByteArrayInputStream(cert)) as X509Certificate)

    //获得公钥
    val publicKey: ByteArray = MessageDigest
            .getInstance(sign)
            .digest(x509Certificate.encoded)

    //字节到十六进制的格式转换
    return byte2HexFormatted(publicKey)
}

//这里是将获取到得编码进行16进制转换
private fun byte2HexFormatted(arr: ByteArray): String {
    val str = StringBuilder(arr.size * 2)
    arr.forEachIndexed { index, it ->
        str.append(String.format("%02X", it))
        if (index != arr.size - 1) {
            str.append(':')
        }
    }
    return str.toString()
}

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
    Typeface::class
            .java
            .getDeclaredField(staticTypefaceFieldName)
            .apply {
                isAccessible = true
                set(null, newTypeface)
            }
}

/**
 * 以短信的方式发送字符串
 */
fun Context.sendSms(smsContent: String) {
    val smsToUri: Uri = Uri.parse("smsto:")
    val intent = Intent(Intent.ACTION_SENDTO, smsToUri)
    intent.putExtra("sms_body", smsContent)
    this.startActivity(intent)
}

/**
 * 安装APK.
 *
 * @param apkPath 安装包的路径
 */
fun Context.installApk(apkPath: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        FileProvider.getUriForFile(this, "${this.packageName}.fileProvider", apkPath.toFile())
    } else {
        apkPath.toUri()
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    this.startActivity(intent)
}

/**
 * 跳转到卸载指定APP的activity
 *
 * @param packageName 包名
 */
fun Context.uninstallApk(packageName: String) {
    this.startActivity(Intent(Intent.ACTION_DELETE, "package:$packageName".toUri()))
}

/**
 * 包装Context用于更换语言
 */
fun Context.changeLanguage(language: String): ContextWrapper {
    val newLocale = Locale(language)
    val configuration = this.resources.configuration

    //首先将设置改变成默认值
    configuration.setToDefaults()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.setLocale(newLocale)
        val localeList = LocaleList(newLocale)
        LocaleList.setDefault(localeList)
        configuration.locales = localeList
    } else {
        configuration.setLocale(newLocale)
    }
    return ContextWrapper(this.createConfigurationContext(configuration))
}

/**
 * 获取电池电量  返回 0-100 的整数
 */
fun Context.getBatteryLevel(): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    val batteryManager: BatteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
} else {
    val intent: Intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
}






