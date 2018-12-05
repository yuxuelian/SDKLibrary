package com.example.share

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QzoneShare
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


/**
 * @author kaibo
 * @date 2018/7/24 10:05
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */
object Share {

    lateinit var tencent: Tencent

    //这个对象是专门用来向微信发送数据的一个重要接口,使用强引用持有,所有的信息发送都是基于这个对象的
    lateinit var iWxApi: IWXAPI

    fun init(applicationContext: Context, qqId: String, wxId: String) {
        tencent = Tencent.createInstance(qqId, applicationContext)
        iWxApi = WXAPIFactory.createWXAPI(applicationContext, wxId, true)
        iWxApi.registerApp(wxId)
    }

    /**
     * 分享到QQ
     */
    fun shareToQQ(activity: Activity, iUiListener: IUiListener) {
        val params = Bundle()
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)//分享的类型
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "然了个然CSDN博客")//分享标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "不管是怎样的过程,最终目的还是那个理想的结果。")//要分享的内容摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://blog.csdn.net/sandyran")//内容地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://avatar.csdn.net/B/3/F/1_sandyran.jpg")//分享的图片URL
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试")//应用名称
        tencent.shareToQQ(activity, params, iUiListener)
    }

    /**
     * 分享到QQ空间
     */
    fun shareToQzon(activity: Activity, iUiListener: IUiListener) {
        val qzoneType = QzoneShare.SHARE_TO_QZONE_TYPE_NO_TYPE
        val params = Bundle()
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, qzoneType)
        //分享标题
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "然了个然CSDN博客")
        //分享的内容摘要
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "不管是怎样的过程,最终目的还是那个理想的结果。")
        //分享的链接
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://blog.csdn.net/sandyran/article/details/53204529")
        //分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）
        val imageUrls = ArrayList<String>()
        imageUrls.add("http://avatar.csdn.net/B/3/F/1_sandyran.jpg")//添加一个图片地址
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls)//分享的图片URL
        tencent.shareToQzone(activity, params, iUiListener)
    }

    /**
     * 分享音乐
     */
    fun qqMusicShare(activity: Activity, iUiListener: IUiListener) {
        val params = Bundle()
        //分享的类型
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
        //分享的标题
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "歌曲名：说穿")
        //分享的链接
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "https://y.qq.com/n/yqq/song/004Dle9I3TaSai.html")
        //分享的图片URL
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "https://y.gtimg.cn/music/photo_new/T002R300x300M000000kbocv24CRbE.jpg?max_age=2592000")
        //应用名称
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试")
        //分享时自动打开分享到QZone的对话框
        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN.toString())
        tencent.shareToQQ(activity, params, iUiListener)
    }

    fun sharePicByFile(picFile: File, tag: String) {
        if (!picFile.exists()) {
            return
        }
        val pic: Bitmap = BitmapFactory.decodeFile(picFile.path)
        //这个构造方法中自动把传入的bitmap转化为2进制数据,或者你直接传入byte[]也行
        //注意传入的数据不能大于10M,开发文档上写的
        val imageObject = WXImageObject(pic)
        //这个对象是用来包裹发送信息的对象
        val msg = WXMediaMessage()
        msg.mediaObject = imageObject
        //msg.mediaObject实际上是个IMediaObject对象,
        //它有很多实现类,每一种实现类对应一种发送的信息,
        //比如WXTextObject对应发送的信息是文字,想要发送文字直接传入WXTextObject对象就行
        val thumbBitmap: Bitmap = Bitmap.createScaledBitmap(pic, 150, 150, true)
        val baos = ByteArrayOutputStream()
        thumbBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        msg.thumbData = baos.toByteArray()
        //在这设置缩略图
        //官方文档介绍这个bitmap不能超过32kb
        //如果一个像素是8bit的话换算成正方形的bitmap则边长不超过181像素,边长设置成150是比较保险的
        //或者使用msg.setThumbImage(thumbBitmap);省去自己转换二进制数据的过程
        //如果超过32kb则抛异常
        //创建一个请求对象
        val req = SendMessageToWX.Req()
        //把msg放入请求对象中
        req.message = msg
        //设置发送到朋友圈
        req.scene = SendMessageToWX.Req.WXSceneTimeline
        //设置发送给朋友
//        req.scene = SendMessageToWX.Req.WXSceneSession
        //这个tag要唯一,用于在回调中分辨是哪个分享请求
        req.transaction = tag
        //如果微信调用成功,会返回true
        val b = iWxApi.sendReq(req)
    }
}