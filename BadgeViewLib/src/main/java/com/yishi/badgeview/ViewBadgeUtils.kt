package com.kaibo.badgeview

import android.view.View

/**
 * @author kaibo
 * @date 2018/12/6 16:58
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

fun View.showBadgeNumber(badgeNumber: Int, dismissCallback: () -> Unit = {}): Badge {
    var viewTarget: Any? = this.tag
    if (viewTarget == null) {
        viewTarget = BadgeView(context)
        this.tag = viewTarget
    } else {
        if (viewTarget !is Badge) {
            throw IllegalStateException("viewTarget is not Badge")
        }
    }

    viewTarget as Badge

    return viewTarget.bindTarget(this).setBadgeNumber(badgeNumber)
            .setOnDragStateChangedListener { dragState, _, _ ->
                if (dragState == Badge.OnDragStateChangedListener.STATE_SUCCEED) {
                    dismissCallback()
                }
            }
}

fun View.showBadgeText(badgeText: String, dismissCallback: () -> Unit = {}): Badge {
    var viewTarget: Any? = this.tag
    if (viewTarget == null) {
        viewTarget = BadgeView(context)
        this.tag = viewTarget
    } else {
        if (viewTarget !is Badge) {
            throw IllegalStateException("viewTarget is not Badge")
        }
    }

    viewTarget as Badge

    return viewTarget.bindTarget(this).setBadgeText(badgeText)
            .setOnDragStateChangedListener { dragState, _, _ ->
                if (dragState == Badge.OnDragStateChangedListener.STATE_SUCCEED) {
                    dismissCallback()
                }
            }
}

fun View.hideBadge(animate: Boolean) {
    val viewTag = this.tag
    if (viewTag == null) {
        // 未显示过 Badge
    } else {
        if (viewTag is Badge) {
            viewTag.hide(animate)
        } else {
            throw IllegalStateException("viewTarget is not Badge")
        }
    }
}
