package com.kaibo.mvp.contract

/**
 * @author:Administrator
 * @date:2018/3/16 0016 下午 1:37
 * GitHub:
 * email:
 * description:
 */
interface IBaseView {

    fun showLoad()

    fun hideLoad()

    fun showInfo(msg: String)

    fun showSuccess(msg: String)

    fun showWarning(msg: String)

    fun showError(msg: String)

}