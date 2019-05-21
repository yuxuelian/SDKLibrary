#############################################
#
# 对于一些基本指令的添加
#
#############################################

# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5
# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames
# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose
# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers
# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify
# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses
# 避免混淆泛型
-keepattributes Signature
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*

#############################################
#
# Android通用混淆配置
#
#############################################

# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.com.kaibo.mvp.view.View
-keep public class com.android.vending.licensing.ILicensingService

# google相关均不混淆
-keep class androidx.** {*;}
-keep class kotlin.** {*;}
-keep class kotlinx.** {*;}
-keep class android.support.** {*;}
-keep class com.google.** {*;}
-keep class com.squareup.** {*;}

# 保留继承的
-keep public class * extends androidx.**
-keep public class * extends kotlin.**
-keep public class * extends kotlinx.**
-keep public class * extends android.support.**
-keep public class * extends com.google.**
-keep public class * extends com.squareup.**

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.com.kaibo.mvp.view.View);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.com.kaibo.mvp.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

#############################################
#
# 自定义混淆配置
#
#############################################
# 不混淆 linphone
-dontwarn org.linphone.**
-keep class org.linphone.** {*;}
-keep interface org.linphone.** {*;}
-keep public class * extends org.linphone.** {*;}

# easyguideview引导层
-dontwarn com.yuyh.library.**
-keep class com.yuyh.library.** {*;}
-keep interface com.yuyh.library.** {*;}
-keep public class * extends com.yuyh.library.** {*;}

# android_spin_kit加载动画库
-dontwarn com.github.ybq.android.spinkit.**
-keep class com.github.ybq.android.spinkit.** {*;}
-keep interface com.github.ybq.android.spinkit.** {*;}
-keep public class * extends com.github.ybq.android.spinkit.** {*;}

# 桌面角标
-dontwarn me.leolin.shortcutbadger.**
-keep class me.leolin.shortcutbadger.** {*;}
-keep interface me.leolin.shortcutbadger.** {*;}
-keep public class * extends me.leolin.shortcutbadger.** {*;}

# 屏幕适配
-dontwarn me.jessyan.autosize.**
-keep class me.jessyan.autosize.** {*;}
-keep interface me.jessyan.autosize.** {*;}
-keep public class * extends me.jessyan.autosize.** {*;}

# ucrop 裁剪混淆
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }
-keep public class * extends com.yalantis.ucrop.** {*;}

# jakewharton相关开源库均不混淆
-dontwarn com.jakewharton.**
-keep class com.jakewharton.** {*;}
-keep interface com.jakewharton.** {*;}
-keep public class * extends com.jakewharton.** {*;}

# okhttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# Retrofit
-dontwarn retrofit2.**
-dontwarn javax.inject.**
-dontwarn javax.annotation.**
-keep class retrofit2.** { *; }

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# 腾讯bugly
-dontwarn com.tencent.bugly.**
-keep class com.tencent.bugly.**{*;}

# zxing
-dontwarn com.google.zxing.**
-keep class com.google.zxing.**{*;}
-keep interface com.google.zxing.**{*;}

# 信鸽推送
-keep class com.tencent.android.tpush.** {* ;}
-keep class com.tencent.mid.** {* ;}
-keep class com.qq.taf.jce.** {*;}
-keep class com.tencent.bigdata.** {* ;}
# 华为通道
-ignorewarning
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.android.hms.agent.**{*;}
# 小米通道
-keep class com.xiaomi.**{*;}
-keep public class * extends com.xiaomi.mipush.sdk.PushMessageReceiver
# 魅族通道
-dontwarn com.meizu.cloud.pushsdk.**
-keep class com.meizu.cloud.pushsdk.**{*;}

# gson
#-dontwarn com.google.**
#-keep class com.google.gson.** {*;}
#-keep class com.google.protobuf.* {*;}

# RSA不混淆
#-dontwarn org.bouncycastle.**
#-keep class org.bouncycastle.**{*;}

# tbs不混淆
#-dontwarn com.tencent.**
#-keep class com.tencent.**{*;}

# 高徳地图
#-dontwarn com.amap.api.**
#-dontwarn com.a.a.**
#-dontwarn com.autonavi.**
#-keep class com.amap.api.**  {*;}
#-keep class com.autonavi.**  {*;}
#-keep class com.a.a.**  {*;}

# Glide
#-dontwarn com.bumptech.glide.**
#-keep class com.bumptech.glide.**{*;}
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public class * extends com.bumptech.glide.AppGlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}

# gif加载不混淆
#-dontwarn pl.droidsonroids.gif.**
#-keep class pl.droidsonroids.gif.* {*;}

# 微信
#-keep class com.tencent.mm.opensdk.** {*;}
#-keep class com.tencent.wxop.** {*;}
#-keep class com.tencent.mm.sdk.** {*;}

# 支付宝
#-dontwarn android.net.**
#-keep class com.alipay.android.app.IAlixPay{*;}
#-keep class com.alipay.android.app.IAlixPay$Stub{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
#-keep class com.alipay.sdk.app.PayTask{ public *;}
#-keep class com.alipay.sdk.app.AuthTask{ public *;}
#-keep class com.alipay.sdk.app.H5PayCallback {
#    <fields>;
#    <methods>;
#}
#-keep class com.alipay.android.phone.mrpc.core.** { *; }
#-keep class com.alipay.apmobilesecuritysdk.** { *; }
#-keep class com.alipay.mobile.framework.service.annotation.** { *; }
#-keep class com.alipay.mobilesecuritysdk.face.** { *; }
#-keep class com.alipay.tscenter.biz.rpc.** { *; }
#-keep class org.json.alipay.** { *; }
#-keep class com.alipay.tscenter.** { *; }
#-keep class com.ta.utdid2.** { *;}
#-keep class com.ut.device.** { *;}
#-keep class android.net.SSLCertificateSocketFactory{*;}
