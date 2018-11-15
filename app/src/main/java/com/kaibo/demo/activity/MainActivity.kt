package com.kaibo.demo.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v4.view.ViewPager
import com.jakewharton.rxbinding2.view.RxView
import com.jaredrummler.android.processes.AndroidProcesses
import com.jaredrummler.android.processes.models.Statm
import com.kaibo.core.toast.ToastUtils
import com.kaibo.core.util.statusBarHeight
import com.kaibo.core.utl.hasExternalStorage
import com.kaibo.core.utl.installApk
import com.kaibo.core.utl.sign
import com.kaibo.demo.adapter.LoopPagerAdapter
import com.kaibo.demo.R
import com.kaibo.ndklib.encrypt.EncryptUtils
import com.kaibo.swipemenulib.activity.BaseSwipeMenuActivity
import com.kaibo.ui.drawable.ClockDrawable
import com.kaibo.ui.drawable.PolygonLapsDrawable
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.menu_layout.*
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
        text.text = EncryptUtils.getInstance().encrypt("123")
        text.text = EncryptUtils.getInstance().decrypt("decrypt")
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
                    ToastUtils.showSuccess("123")
//            mPresenter.queryOrderById(123L)

                    AndroidProcesses
                            .getRunningProcesses()
                            .forEach {
                                val statm: Statm = it.statm()
                                println(statm.size)
                                println(it.name)
                                println(it.pid)
                                println(it.attr_current())
//                println(it.cgroup().getGroup())
                                println(it.cmdline())
                                println(it.oom_adj())
                                println(it.oom_score())
                                println(it.wchan())
                            }

                    println("包名:${this.packageName}")

                    val rxPermissions = RxPermissions(this)
                    if (!rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        rxPermissions
                                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                                .`as`(bindLifecycle())
                                .subscribe {
                                    if (it) {
                                        //授权成功
                                        Logger.d("授权成功")
                                        if (hasExternalStorage()) {
                                            val apkPath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "app-release.apk"
                                            installApk(File(apkPath))
                                        }
                                    } else {
                                        Logger.d("权限被拒绝")
                                    }
                                }
                    } else {
                        Logger.d("已经授权过了")
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

        loopPager.adapter = LoopPagerAdapter(this, data)
        //默认选择到第一张
        loopPager.setCurrentItem(1, false)
        loopPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var position = 0

            override fun onPageScrollStateChanged(state: Int) {
                Logger.d("---------$state")
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (position == data.size - 1) {
                        //已经到了最后一张的时候,这种情况手动滑动和自动滑动都会出现,主动将currentItem移动到1的位置去
                        loopPager.setCurrentItem(1, false)
                    } else if (position == 0) {
                        //移动到最后一张   这种情况只有手动滑动的时候才会出现,将currentItem移动到倒数第二张
                        loopPager.setCurrentItem(data.size - 2, false)
                    }
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                this.position = position
            }
        })

//        loopPager.setDuration(1000)

        //启动轮播
        Observable
                .interval(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(bindLifecycle())
                .subscribe {
                    loopPager.currentItem = loopPager.currentItem + 1
                }
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
