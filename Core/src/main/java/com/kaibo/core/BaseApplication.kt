package com.kaibo.core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Process
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.kaibo.core.activity.UpgradeActivity
import com.kaibo.core.toast.showError
import com.kaibo.core.toast.showInfo
import com.kaibo.core.toast.showSuccess
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.android.tpush.XGIOperateCallback
import com.tencent.android.tpush.XGPushConfig
import com.tencent.android.tpush.XGPushManager
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.UpgradeInfo
import com.tencent.bugly.beta.interfaces.BetaPatchListener
import com.tencent.bugly.beta.upgrade.UpgradeListener
import com.tencent.bugly.beta.upgrade.UpgradeStateListener
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import com.kaibo.core.util.getProcessName
import com.kaibo.core.util.isNewVersion
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.onAdaptListener
import java.util.*


/**
 * @author:Administrator
 * @date:2018/3/16 0016 下午 1:45
 * GitHub:
 * email:
 * description:
 */

private lateinit var AppInstance: BaseApplication

object AppContext : ContextWrapper(AppInstance)

abstract class BaseApplication : Application() {

    companion object {
        private const val BUGLY_APP_ID = "8a0b32b82e"

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    protected var isMainProcess = false

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
        // 安装tinker
        Beta.installTinker()
    }

    // 设置APP为严格模式运行
    private fun setStrictMode() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build())
    }

    override fun onCreate() {
        super.onCreate()
        // 标记当前是否是主进程
        isMainProcess = getProcessName(Process.myPid()) == packageName
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return
//        }

//        if (BuildConfig.DEBUG) {
//            LeakCanary.install(this)
//        }

        AppInstance = this
        Logger.addLogAdapter(AndroidLogAdapter())

        initAutoSize()

        initTinker()
//        initPush()
        initBugly()
    }

    private fun initTinker() {
//        setStrictMode()
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = true
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true
        // 补丁回调接口，可以监听补丁接收、下载、合成的回调
        Beta.betaPatchListener = object : BetaPatchListener {
            override fun onPatchReceived(patchFileUrl: String) {
                showInfo(patchFileUrl)
            }

            override fun onDownloadReceived(savedLength: Long, totalLength: Long) {
                showInfo(String.format(Locale.CHINESE,
                        "%s %d",
                        Beta.strNotificationDownloading,
                        (if (totalLength == 0L) 0 else savedLength * 100 / totalLength).toInt()))
            }

            override fun onDownloadSuccess(patchFilePath: String) {
                showInfo(patchFilePath)
                //                Beta.applyDownloadedPatch();
            }

            override fun onDownloadFailure(msg: String) {
                showInfo(msg)
            }

            override fun onApplySuccess(msg: String) {
                showInfo(msg)
            }

            override fun onApplyFailure(msg: String) {
                showInfo(msg)
            }

            override fun onPatchRollback() {
                showInfo("onPatchRollback")
            }
        }
    }

    private fun initBugly() {
        Beta.upgradeListener = UpgradeListener { _, upgradeInfo: UpgradeInfo?, _, _ ->
            if (upgradeInfo != null) {
                // 启动自定义的更新Activity
                val intent = Intent(this, UpgradeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        Beta.upgradeStateListener = object : UpgradeStateListener {
            override fun onUpgrading(isManual: Boolean) {
                if (isManual) {
                    showSuccess("正在检查更新...")
                }
            }

            override fun onUpgradeSuccess(isManual: Boolean) {
                isNewVersion = true
                showSuccess("检查到新版本")
            }

            override fun onDownloadCompleted(isManual: Boolean) {
                if (isManual) {
                    showSuccess("下载新版本完成")
                }
            }

            override fun onUpgradeNoVersion(isManual: Boolean) {
                isNewVersion = false
                if (isManual) {
                    showSuccess("当前已是最新版本")
                }
            }

            override fun onUpgradeFailed(isManual: Boolean) {
                isNewVersion = false
                if (isManual) {
                    showError("检查新版本失败")
                }
            }
        }
        // 自动初始化
        Beta.autoInit = true
        // 检查间隔
        Beta.upgradeCheckPeriod = 0
        // 延迟500ms初始化
        Beta.initDelay = 0
        // 每次启动都可以显示提示
        Beta.showInterruptedStrategy = true
        // 显示通知栏
        Beta.enableNotification = true
        // 初始化完成自动检查更新
        Beta.autoCheckUpgrade = false

        // Wifi自动下载
//        Beta.autoDownloadOnWifi = true

        // bugly初始化
        val start = System.currentTimeMillis()
        // 设置是否为上报进程
        val strategy = UserStrategy(this).apply {
            // 只有主进程才上传异常日志
            isUploadProcess = isMainProcess
            appChannel = "debug"
        }
        // 单独的bugly初始化
//        CrashReport.initCrashReport(this, BaseApplication.BUGLY_APP_ID, BuildConfig.DEBUG, strategy)
        // 集成版本升级的bugly
        Bugly.init(this, BUGLY_APP_ID, BuildConfig.DEBUG, strategy)
        val end = System.currentTimeMillis()
        Log.e("init Bugly time--->", (end - start).toString() + "ms")
    }

    private fun initAutoSize() {
        AutoSizeConfig.getInstance()
                //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
                //如果没有这个需求建议不开启
                .setCustomFragment(true)
                //屏幕适配监听器
                .setOnAdaptListener(object : onAdaptListener {
                    override fun onAdaptBefore(target: Any, activity: Activity) {
                        //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                        //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
//                        AutoSizeConfig.getInstance().screenWidth = ScreenUtils.getScreenSize(activity)[0]
//                        AutoSizeConfig.getInstance().screenHeight = ScreenUtils.getScreenSize(activity)[1]
//                        Logger.d("${activity.javaClass.name} onAdaptBefore!")
//                        Logger.d("${target.javaClass.name} onAdaptBefore!")
                    }

                    override fun onAdaptAfter(target: Any, activity: Activity) {
//                        Logger.d("${activity.javaClass.name} onAdaptAfter!")
//                        Logger.d("${target.javaClass.name} onAdaptAfter!")
                    }
                })//是否打印 AutoSize 的内部日志, 默认为 true, 如果您不想 AutoSize 打印日志, 则请设置为 false
                .setLog(true)
                //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
                //AutoSize 会将屏幕总高度减去状态栏高度来做适配
                //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
                .setUseDeviceSize(true)
        //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
//                .setBaseOnWidth(false)
        //设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好
//                .setAutoAdaptStrategy { target, activity ->
//
//                }

        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        //在 Demo 中跳转的三方库中的 DefaultErrorActivity 就是在另外一个进程中, 所以要想适配这个 Activity 就需要调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(this)

        AutoSizeConfig.getInstance()
                .externalAdaptManager
        //加入的 Activity 将会放弃屏幕适配, 一般用于三方库的 Activity, 详情请看方法注释
        //如果不想放弃三方库页面的适配, 请用 addExternalAdaptInfoOfActivity 方法, 建议对三方库页面进行适配, 让自己的 App 更完美一点
        //                .addCancelAdaptOfActivity(DefaultErrorActivity.class)
        //为指定的 Activity 提供自定义适配参数, AndroidAutoSize 将会按照提供的适配参数进行适配, 详情请看方法注释
        //一般用于三方库的 Activity, 因为三方库的设计图尺寸可能和项目自身的设计图尺寸不一致, 所以要想完美适配三方库的页面
        //就需要提供三方库的设计图尺寸, 以及适配的方向 (以宽为基准还是高为基准?)
        //三方库页面的设计图尺寸可能无法获知, 所以如果想让三方库的适配效果达到最好, 只有靠不断的尝试
        //由于 AndroidAutoSize 可以让布局在所有设备上都等比例缩放, 所以只要您在一个设备上测试出了一个最完美的设计图尺寸
        //那这个三方库页面在其他设备上也会呈现出同样的适配效果, 等比例缩放, 所以也就完成了三方库页面的屏幕适配
        //即使在不改三方库源码的情况下也可以完美适配三方库的页面, 这就是 AndroidAutoSize 的优势
        //但前提是三方库页面的布局使用的是 dp 和 sp, 如果布局全部使用的 px, 那 AndroidAutoSize 也将无能为力
        //经过测试 DefaultErrorActivity 的设计图宽度在 380dp - 400dp 显示效果都是比较舒服的
//                .addExternalAdaptInfoOfActivity(UCropActivity::class.java, ExternalAdaptInfo(true, 360f))
    }

    private fun initPush() {
        // 是否启动调试信息
        XGPushConfig.enableDebug(this, true)
        // 注册监听
        XGPushManager.registerPush(this, object : XGIOperateCallback {
            override fun onSuccess(data: Any, flag: Int) {
                //token在设备卸载重装的时候有可能会变
                Log.d("TPush", "注册成功，设备token为：$data")
            }

            override fun onFail(data: Any, errorCode: Int, msg: String) {
                Log.d("TPush", "注册失败，错误码：$errorCode,错误信息：$msg")
            }
        })
    }
}
