package com.kaibo.demo.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import com.jakewharton.rxbinding2.view.RxView
import com.kaibo.core.activity.BaseSwipeMenuActivity
import com.kaibo.core.toast.showSuccess
import com.kaibo.core.util.bindLifecycle
import com.kaibo.core.util.statusBarHeight

import com.kaibo.demo.R
import com.kaibo.view.drawable.ClockDrawable
import com.kaibo.view.drawable.PolygonLapsDrawable
import com.orhanobut.logger.Logger
import com.kaibo.core.util.hasExternalStorage
import com.kaibo.core.util.installApk
import com.kaibo.core.util.sign
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.concurrent.TimeUnit

class MainActivity : BaseSwipeMenuActivity() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun getSlideMenuLayout(): Int {
        return R.layout.menu_layout
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
//        text.text = EncryptUtils.getInstance().encrypt("123")
//        text.text = EncryptUtils.getInstance().decrypt("decrypt")
//        println(isRoot())
//        println(isEmulator())
//        println(hasExternalStorage())
//        println(versionCode)
//        println(versionName)
//        println(isGPSEnable)
//        println(isNetworkConnected)
//        println(sha1)

//        MD5:  BF:4F:43:B0:43:93:1A:54:59:A8:1B:B3:C6:51:52:48
//        SHA1: 70:F5:EA:30:D9:BB:CB:0E:5E:C9:5A:D7:90:0B:31:29:71:8F:09:D9

        appBarLayout.setPadding(0, statusBarHeight, 0, 0)

//        val textChanges = RxTextView.textChanges(editText)
//        val textChanges2 = RxTextView.textChanges(editText2)
//        Observable
//                .combineLatest(textChanges, textChanges2, BiFunction<CharSequence, CharSequence, Boolean> { text1, text2 ->
//                    Logger.d("text1=$text1")
//                    Logger.d("text2=$text2")
//                    text1.isNotEmpty() && text2.isNotEmpty()
//                })
//                .subscribe {
//
//                }

        RxView
                .clicks(resume)
                .`as`(bindLifecycle())
                .subscribe {
                    //                    startFloatingService()
                }

        RxView
                .clicks(button)
                .debounce(200L, TimeUnit.MILLISECONDS)
                .`as`(bindLifecycle())
                .subscribe { _ ->
                    println(sign("SHA1"))
                    showSuccess("123")
//            mPresenter.queryOrderById(123L)

                    easyRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, reject = {
                        // 权限被拒绝

                    }) {
                        //授权成功
                        Logger.d("授权成功")
                        if (hasExternalStorage()) {
                            val apkPath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "app-release.apk"
                            installApk(File(apkPath))
                        }
                    }

//            AndroidProcesses.getRunningAppProcesses().forEach {
//                val stat: Stat = it.stat()
//                println(stat.comm)
//                println(stat.pid)
//                println(stat.ppid())
//                println(stat.stime())
//                println(stat.state())
//            }
                }

        roundDrawableTest()
//        polygonTest()

        val data = intArrayOf(R.drawable.a, R.drawable.b).toMutableList()
        val first = data.last()
        val last = data.first()
        data.add(0, first)
        data.add(last)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        checkOverlayPermission(requestCode, resultCode, data)
    }

    private fun roundDrawableTest() {
//        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.navigation_icon)
//        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
//        roundedBitmapDrawable.setAntiAlias(true)
//        //        roundedBitmapDrawable.isCircular = true
//        roundedBitmapDrawable.cornerRadius = 50F
//        imageView.setImageDrawable(roundedBitmapDrawable)
//        imageView.setImageDrawable(RoundImageDrawable(bitmap, 50F))

//        val circularProgressDrawable = CircularProgressDrawable(this)
//        circularProgressDrawable.setColorSchemeColors(Color.RED, Color.BLUE)
//        circularProgressDrawable.strokeWidth = 20F
//        imageView.setImageDrawable(circularProgressDrawable)
//        circularProgressDrawable.start()

        //时钟drawable
        val clockDrawable = ClockDrawable(resources)
        clockDrawable.start()
        imageView.setImageDrawable(clockDrawable)
    }

    private fun polygonTest() {
        val drawable = PolygonLapsDrawable()
        drawable.start()
        //需要先设置到ImageView
        imageView.setImageDrawable(drawable)
        resume.setOnClickListener {
            drawable.resume()
        }

        pause.setOnClickListener {
            drawable.pause()
        }
    }

}
