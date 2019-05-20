package com.kaibo.core.util

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.kaibo.core.R
import com.kaibo.core.toast.showError
import com.yalantis.ucrop.UCrop
import java.io.File

/**
 * @author kaibo
 * @date 2018/11/15 11:16
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

class BitmapResultFragment : Fragment() {

    companion object {
        private const val REQUEST_SELECT_PIC = 0x01
        private const val REQUEST_TAKE_PHOTO = 0x02
    }

    private var mCropCallBack: ((Uri?) -> Unit)? = null
    private var mSelectCallBack: ((Uri?) -> Unit)? = null

    /**
     * 相机拍照的临时存储文件
     */
    private val takePhotoFile by lazy {
        // 系统相机目录
        val photoPath = Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_DCIM + File.separator
        // 创建需要保存的图片文件
        File(photoPath, "take-photo-file.jpg")
    }

    /**
     * Fragment中启动裁剪
     */
    fun startCrop(sourceUri: Uri, callBack: ((Uri?) -> Unit)? = null) {
        mCropCallBack = callBack
        val context = requireContext()
        val options = UCrop.Options()
        val themeColor = ContextCompat.getColor(context, R.color.theme_blue)
        options.setToolbarColor(themeColor)
        options.setStatusBarColor(themeColor)
        // 显示圆形覆盖层
        options.setCircleDimmedLayer(true)
        options.setHideBottomControls(false)
        UCrop
                .of(sourceUri, Uri.fromFile(File(context.filesDir, "destination-crop-bitmap.jpg")))
                .withAspectRatio(1f, 1f)
                .withOptions(options)
                .withMaxResultSize(500, 500)
                .start(context, this)
    }

    /**
     * 启动原生的图片选择器
     */
    fun openPhotoAlbum(callBack: ((Uri?) -> Unit)? = null) {
        mSelectCallBack = callBack
        val context = requireContext()
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(context.packageManager) != null) {
            //打开系统相册
            startActivityForResult(intent, REQUEST_SELECT_PIC)
        } else {
            context.showError("无法打开相册")
        }
    }

    /**
     * 打开相机拍照
     */
    fun openCamera(callBack: ((Uri?) -> Unit)? = null) {
        mSelectCallBack = callBack
        val context = requireContext()
        // 启动相机的Intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(context.packageManager) != null) {
            // 兼容android 7.0
            val photoURI: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", takePhotoFile)
            } else {
                Uri.fromFile(takePhotoFile)
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, REQUEST_TAKE_PHOTO)
        } else {
            context.showError("无法启动相机")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                UCrop.REQUEST_CROP -> {
                    //裁剪成功
                    data?.let { intent ->
                        UCrop.getOutput(intent)?.let {
                            mCropCallBack?.invoke(it)
                        }
                    }
                    // 必须置为null   否则有内存泄漏的风险
                    mCropCallBack = null
                }
                REQUEST_SELECT_PIC -> {
                    // 相册返回
                    data?.let {
                        // 获取选择的图片的路径
                        val sourceFilePath = requireContext().handleSelectIntent(it)
                        if (sourceFilePath.isNotEmpty()) {
                            // 回调选择成功
                            mSelectCallBack?.invoke(Uri.fromFile(File(sourceFilePath)))
                            mSelectCallBack = null
                            return
                        }
                    }
                    // 未选择
                    mSelectCallBack?.invoke(null)
                    mSelectCallBack = null
                }
                REQUEST_TAKE_PHOTO -> {
                    // 拍照返回
                    mSelectCallBack?.invoke(Uri.fromFile(takePhotoFile))
                    mSelectCallBack = null
                }
            }
            UCrop.RESULT_ERROR -> {
                data?.let {
                    val error = UCrop.getError(it)
                    error?.printStackTrace()
                }
                // 裁剪失败
                mCropCallBack?.invoke(null)
                mCropCallBack = null
            }
            Activity.RESULT_CANCELED -> {
                // 取消图片选择  或者拍照取消
                mSelectCallBack?.invoke(null)
                mSelectCallBack = null

                // 取消裁剪
                mCropCallBack?.invoke(null)
                mCropCallBack = null
            }
        }
    }

    /**
     * 获取选择的图片的路径
     */
    private fun Context.handleSelectIntent(data: Intent): String {
        return data.data?.let { uri: Uri ->
            when {
                DocumentsContract.isDocumentUri(this, uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    //如果是document类型的Uri,则通过document id处理
                    when {
                        "com.android.providers.media.documents" == uri.authority -> {
                            val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                            val selection = MediaStore.Images.Media._ID + "=" + id
                            getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
                        }
                        "com.android.providers.downloads.documents" == uri.authority -> {
                            val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), docId.toLong())
                            getImagePath(contentUri, null)
                        }
                        else -> ""
                    }
                }
                //如果是content类型的Uri，则使用普通方式处理
                "content".equals(uri.scheme, ignoreCase = true) -> getImagePath(uri, null)
                //如果是file类型的Uri，直接获取图片路径即可
                "file".equals(uri.scheme, ignoreCase = true) -> uri.path
                else -> ""
            }
        } ?: ""
    }

    /**
     * 获取选择图片的路径
     */
    private fun Context.getImagePath(uri: Uri, selection: String?): String {
        var path = ""
        val cursor: Cursor? = this.contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }
}

class PictureCropUtils(fragmentActivity: FragmentActivity) {

    companion object {
        private const val FRAGMENT_TAG = "PictureCropUtils"
    }

    private val bitmapResultFragment: BitmapResultFragment

    init {
        val supportFragmentManager = fragmentActivity.supportFragmentManager
        val findFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as? BitmapResultFragment
        bitmapResultFragment = if (findFragment != null) {
            findFragment
        } else {
            val tempFragment = BitmapResultFragment()
            supportFragmentManager.beginTransaction().add(tempFragment, FRAGMENT_TAG).commitNowAllowingStateLoss()
            tempFragment
        }
    }

    fun startCrop(sourceUri: Uri, callBack: ((Uri?) -> Unit)? = null) {
        bitmapResultFragment.startCrop(sourceUri, callBack)
    }

    fun openCamera(callBack: ((Uri?) -> Unit)? = null) {
        bitmapResultFragment.openCamera(callBack)
    }

    fun openPhotoAlbum(callBack: ((Uri?) -> Unit)? = null) {
        bitmapResultFragment.openPhotoAlbum(callBack)
    }
}
