package com.kaibo.demo.mvp.model

import com.kaibo.base.mvp.model.AbstractModel
import com.kaibo.demo.mvp.contract.MainContract

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:36
 * GitHub：
 * email：
 * description：model实现类
 */

class MainModel : AbstractModel(), MainContract.Model {

    override fun getTestStr() = "测试字符串"

}