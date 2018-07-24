package com.example.share.wxapi

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.share.Share
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * @author kaibo
 * @date 2018/7/24 10:43
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：这个Activity需要连同报名拷贝到主包目录下
 */

class WXEntryActivity : AppCompatActivity(), IWXAPIEventHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //处理微信传回的Intent,当然你也可以在别的地方处理
        Share.iWxApi.handleIntent(intent, this)
    }

    override fun onResp(baseResp: BaseResp) {
        //形参resp 有下面两个个属性比较重要
        //1.resp.errCode
        //2.resp.transaction则是在分享数据的时候手动指定的字符创,用来分辨是那次分享(参照4.中req.transaction)
        when (baseResp.errCode) { //根据需要的情况进行处理
        //正确返回
            BaseResp.ErrCode.ERR_OK -> {

            }
        //用户取消
            BaseResp.ErrCode.ERR_USER_CANCEL -> {

            }
        //认证被否决
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {

            }
        //发送失败
            BaseResp.ErrCode.ERR_SENT_FAILED -> {

            }
        //不支持错误
            BaseResp.ErrCode.ERR_UNSUPPORT -> {

            }
        //一般错误
            BaseResp.ErrCode.ERR_COMM -> {

            }
        //其他不可名状的情况
            else -> {

            }
        }
    }

    override fun onReq(baseReq: BaseReq) {

    }
}