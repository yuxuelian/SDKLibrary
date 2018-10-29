package com.kaibo.core.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.FileProvider
import android.view.inputmethod.InputMethodManager
import com.kaibo.core.R
import com.kaibo.core.toast.ToastUtils
import java.io.File

/**
 * @author kaibo
 * @date 2018/6/26 10:31
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) =
        startActivity(this.createIntent(T::class.java, params))

inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) =
        startActivityForResult(this.createIntent(T::class.java, params), requestCode)

inline fun <reified T : Activity> Activity.animStartActivity(vararg params: Pair<String, Any?>) {
    startActivity<T>(*params)
    overridePendingTransition(R.anim.translation_right_in, R.anim.translation_right_out)
}

inline fun <reified T : Activity> Activity.animStartActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) {
    startActivityForResult<T>(requestCode, *params)
    overridePendingTransition(R.anim.translation_right_in, R.anim.translation_right_out)
}

fun FragmentActivity.addFragmentToActivity(@IdRes frameId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
}

/**
 * 隐藏软键盘
 */

fun Activity.hideSoftInput() {
    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

/**
 * 启动原生的图片选择器
 */
fun Activity.openPhotoAlbum(requestCode: Int) {
    val intent = Intent("android.intent.action.GET_CONTENT")
    intent.type = "image/*"
    //打开系统相册
    startActivityForResult(intent, requestCode)
    overridePendingTransition(R.anim.translation_right_in, R.anim.translation_right_out)
}

/**
 * 启动原生的图片选择器
 */
fun Fragment.openPhotoAlbum(requestCode: Int) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    //打开系统相册
    startActivityForResult(intent, requestCode)
}

/**
 * 打开相机拍照
 */
fun Activity.openCamera(photoFile: File, requestCode: Int) {
    // 启动相机的Intent
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    // 是否可以启动相机
    if (takePictureIntent.resolveActivity(packageManager) != null) {
        // 兼容android 7.0
        val photoURI = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(this, "$packageName.fileProvider", photoFile)
        } else {
            Uri.fromFile(photoFile)
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(takePictureIntent, requestCode)
    }
}

/**
 * 将新增的图片添加到系统的路径去
 */
private fun Context.galleryAddPic(photoFile: File) {
    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    //mCurrentPhotoPath即文件的路径
    val contentUri = Uri.fromFile(photoFile)
    mediaScanIntent.data = contentUri
    this.sendBroadcast(mediaScanIntent)
}

/**
 * 打开相机拍照
 */
fun Fragment.openCamera(photoFile: File, requestCode: Int) {
    // 启动相机的Intent
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    // 是否可以启动相机
    if (context != null && takePictureIntent.resolveActivity(context!!.packageManager) != null) {
        // 兼容android 7.0
        val photoURI: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context!!, "${context!!.packageName}.fileProvider", photoFile)
        } else {
            Uri.fromFile(photoFile)
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(takePictureIntent, requestCode)
    } else {
        ToastUtils.showError("没有相机,无法启动")
    }
}

