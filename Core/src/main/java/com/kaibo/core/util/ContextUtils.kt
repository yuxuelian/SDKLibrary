package com.kaibo.core.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Typeface
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.preference.PreferenceManager
import android.provider.Settings
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.kaibo.core.util.toUri
import com.orhanobut.logger.Logger
import java.io.*
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import kotlin.collections.ArrayList


/**
 * @author:Administrator
 * @date:2018/4/20 0020 下午 2:42
 * GitHub:
 * email:
 * description:
 */


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


@SuppressLint("HardwareIds")
fun Context.getAndroidId(): String {
    val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    return androidId
}

fun Activity.hasNavigationBar() = windowManager.defaultDisplay.run {
    val point1 = Point()
    val point2 = Point()
    getSize(point1)
    getRealSize(point2)
    point1.y != point2.y
}

/**
 * 检测是否为模拟器.
 *
 * @return true表示模拟器
 */
fun Context.isEmulator(): Boolean {
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
fun Context.isRoot() = File("/system/bin/su").exists() || File("/system/xbin/su").exists()

/**
 * 获取当前APP的SHA1签名信息
 * @sign SHA1  MD5
 * @return
 */
fun Context.sign(sign: String = "SHA1"): String {
    @SuppressLint("PackageManagerGetSignatures")
    val cert: ByteArray = this.packageManager.getPackageInfo(this.packageName, PackageManager.GET_SIGNATURES).signatures[0].toByteArray()
    //X509证书，X.509是一种非常通用的证书格式
    val x509Certificate = CertificateFactory.getInstance("X509").generateCertificate(ByteArrayInputStream(cert)) as X509Certificate
    //获得公钥
    val publicKey: ByteArray = MessageDigest.getInstance(sign).digest(x509Certificate.encoded)
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
 * @param staticTypefaceFieldName 被替换掉的系统字体类型
 * @param fontName           用于替换的字体文件名(文件放在assets目录下)
 */
fun Context.setFont(staticTypefaceFieldName: String, fontName: String) {
    val regular: Typeface = Typeface.createFromAsset(assets, fontName)
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
    Typeface::class.java
            .getDeclaredField(staticTypefaceFieldName)
            .apply {
                isAccessible = true
                set(null, newTypeface)
            }
}

/**
 * 以短信的方式发送字符串
 */
fun Context.sendStringBySms(smsContent: String) {
    val sendIntent = Intent(Intent.ACTION_SENDTO, "smsto:".toUri())
    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    sendIntent.putExtra("sms_body", smsContent)
    this.startActivity(sendIntent)
}

fun Context.shareBitmap(bitmap: Bitmap) {
    val type = "image/*"
    val qrCodeBitmapFile = File(Environment.getExternalStorageDirectory(), "qr_code_bitmap.png")
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(qrCodeBitmapFile))
    val imgUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //7.0以上使用FileProvider
        FileProvider.getUriForFile(this, "${this.packageName}.fileProvider", qrCodeBitmapFile)
    } else {
        Uri.fromFile(qrCodeBitmapFile)
    }
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = type
    val intentList: MutableList<Intent> = ArrayList()

    //记录一下上一次的包名
    var lastPackage = ""

    packageManager.queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY).forEach {
        val packageName = it.activityInfo.packageName
//			intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");//微信朋友
//			intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");//微信朋友圈，仅支持分享图片
//			intent.setClassName("com.tencent.mobileqq", "cooperation.qqfav.widget.QfavJumpActivity");//保存到QQ收藏
//			intent.setClassName("com.tencent.mobileqq", "cooperation.qlink.QlinkShareJumpActivity");//QQ面对面快传
//			intent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.qfileJumpActivity");//传给我的电脑
//          intent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");//QQ好友或QQ群

        if (lastPackage != packageName) {
            // 这里可以根据实际需要进行过滤
            if (packageName.toLowerCase().contains("mms") ||
                    packageName.toLowerCase().contains("messaging") ||
                    packageName.toLowerCase().contains("contacts")) {
                //短信
                val intent = Intent(Intent.ACTION_SEND)
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.type = type
                intent.putExtra(Intent.EXTRA_STREAM, imgUri)
                intent.setPackage(packageName)
                intentList.add(intent)
            } else if (packageName == "com.tencent.mm") {
                //微信
                val intent = Intent(Intent.ACTION_SEND)
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.type = type
                intent.putExtra(Intent.EXTRA_STREAM, imgUri)
                intent.setPackage(packageName)
                //分享到微信好友
                intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI")
                intentList.add(intent)
                lastPackage = packageName
            } else if (packageName == "com.tencent.mobileqq") {
                //手机qq
                val intent = Intent(Intent.ACTION_SEND)
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.type = type
                intent.putExtra(Intent.EXTRA_STREAM, imgUri)
                intent.setPackage(packageName)
                //分享到qq好友
                intent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity")
                intentList.add(intent)
                lastPackage = packageName
            }
        }
    }
    val openInChooser: Intent = Intent.createChooser(intentList.removeAt(0), "请选择您要分享的方式")
    openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toTypedArray())
    startActivity(openInChooser)

//    不加限制分享
//    val shareFile = File(Environment.getExternalStorageDirectory(), "share_qr_code_temp.png")
//    bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(shareFile))
//    val imgUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//        //7.0以上使用FileProvider
//        FileProvider.getUriForFile(this, "${this.packageName}.fileProvider", shareFile)
//    } else {
//        Uri.fromFile(shareFile)
//    }
//    val shareIntent = Intent(Intent.ACTION_SEND)
//    shareIntent.type = "image/*"
//    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//    shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri)
//    startActivity(Intent.createChooser(shareIntent, "分享到"))
}

/**
 * 分享多张图片
 */
fun Context.shareMultipleBitmap(bitmapList: List<Bitmap>) {
    val uriList: ArrayList<Uri> = ArrayList(bitmapList.mapIndexed { index, bitmap ->
        val shareFile = File(Environment.getExternalStorageDirectory(), "share_qr_code_temp$index.png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(shareFile))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0以上使用FileProvider
            FileProvider.getUriForFile(this, "${this.packageName}.fileProvider", shareFile)
        } else {
            Uri.fromFile(shareFile)
        }
    })

    val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
    shareIntent.type = "image/*"
    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList)
    startActivity(Intent.createChooser(shareIntent, "分享到"))
}

/**
 * 安装APK.
 *
 * @param apkPath 安装包的路径
 */
fun Context.installApk(apkFile: File) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        FileProvider.getUriForFile(this, "${this.packageName}.fileProvider", apkFile)
    } else {
        Uri.fromFile(apkFile)
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    this.startActivity(intent)
}

/**
 * 卸载APP
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
 * 拨打电话（跳转到拨号界面，用户手动点击拨打）
 *
 * @param phoneNum 电话号码
 */
fun Context.callPhone(phoneNum: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNum")
    this.startActivity(intent)
}

@ColorInt
fun Context.getCompatColor(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}


private const val IS_FIRST_LUNCH = "IS_FIRST_LUNCH"
private const val IS_NEW_VERSION = "IS_NEW_VERSION"

var Context.isNewVersion: Boolean
    get() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        // 检查是否有新版本  默认返回false即没有新版本
        return sharedPreferences.getBoolean(IS_NEW_VERSION, false)
    }
    set(value) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.edit().putBoolean(IS_NEW_VERSION, value).apply()
    }

/**
 * 判断是否是首次启动
 * 这个方法只有在第一次调用的时候才会返回true
 * 以后都会返回false
 */
fun Context.isFirstLunch(): Boolean {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val lunch = sharedPreferences.getBoolean(IS_FIRST_LUNCH, true)
    sharedPreferences.edit().putBoolean(IS_FIRST_LUNCH, false).apply()
    return lunch
}

/**
 * 清除所有数据
 */
@SuppressLint("ApplySharedPref")
fun Context.clearAll() {
    File("/data/data/${this.packageName}").listFiles().forEach {
        if (
                it.absolutePath.endsWith("/files") ||
                it.absolutePath.endsWith("/cache") ||
                it.absolutePath.endsWith("/shared_prefs")
        ) {
            deleteAllFile(it)
        }
    }
    PreferenceManager
            .getDefaultSharedPreferences(this)
            .edit()
            .clear()
            .commit()
}

private fun deleteAllFile(file: File) {
    if (file.isDirectory) {
        file.listFiles().forEach {
            deleteAllFile(it)
        }
    }
    file.delete()
}

fun <T> Context.createIntent(clazz: Class<out T>, params: Array<out Pair<String, Any?>>): Intent {
    val intent = Intent(this, clazz)
    if (params.isNotEmpty()) fillIntentArguments(intent, params)
    return intent
}

private fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
    params.forEach {
        val value = it.second
        when (value) {
            null -> intent.putExtra(it.first, null as Serializable?)
            is Int -> intent.putExtra(it.first, value)
            is Long -> intent.putExtra(it.first, value)
            is CharSequence -> intent.putExtra(it.first, value)
            is String -> intent.putExtra(it.first, value)
            is Float -> intent.putExtra(it.first, value)
            is Double -> intent.putExtra(it.first, value)
            is Char -> intent.putExtra(it.first, value)
            is Short -> intent.putExtra(it.first, value)
            is Boolean -> intent.putExtra(it.first, value)
            is Serializable -> intent.putExtra(it.first, value)
            is Bundle -> intent.putExtra(it.first, value)
            is Parcelable -> intent.putExtra(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                else -> throw RuntimeException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            is IntArray -> intent.putExtra(it.first, value)
            is LongArray -> intent.putExtra(it.first, value)
            is FloatArray -> intent.putExtra(it.first, value)
            is DoubleArray -> intent.putExtra(it.first, value)
            is CharArray -> intent.putExtra(it.first, value)
            is ShortArray -> intent.putExtra(it.first, value)
            is BooleanArray -> intent.putExtra(it.first, value)
            else -> throw RuntimeException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
        }
        return@forEach
    }
}

// 启动将当前应用添加到电源白名单对话框
fun Context.ignoreBatteryOptimization() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val hasIgnored = powerManager.isIgnoringBatteryOptimizations(packageName)
        if (!hasIgnored) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        } else {
            Logger.d("已经被允许在后台运行")
        }
    }
}

// 跳转到电源白名单页面
fun Context.startIgnoreBattery() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        startActivity(intent)
    }
}

// 是否开启通知权限
fun Context.enableNotification(): Boolean {
    val notificationManagerCompat = NotificationManagerCompat.from(this)
    return notificationManagerCompat.areNotificationsEnabled()
}

// 不关闭返回桌面
fun Context.gotoHome() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    startActivity(intent)
}


/**
 * 获取进程号对应的进程名
 *
 * @param pid 进程号
 * @return 进程名
 */
fun Context.getProcessName(pid: Int): String {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.runningAppProcesses.forEach {
        if (it.pid == pid) {
            return it.processName
        }
    }
    return ""
}


/**
 * 判断某个进程是否正在运行
 */
fun Context.isRunningProcess(processKey: String): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.runningAppProcesses.forEach {
        if (it.processName.contains(processKey, true)) {
            return true
        }
    }
    return false
}

/**
 * 判断某个服务是否正在运行
 */
fun Context.isRunningService(serviceKey: String): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.getRunningServices(1000).forEach {
        if (it.service.className.contains(serviceKey, true)) {
            return true
        }
    }
    return false
}
