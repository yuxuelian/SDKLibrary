package com.kaibo.base.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * This provides methods to help Activities load their UI.
 */

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 *
 */
fun FragmentActivity.addFragmentToActivity(frameId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().add(frameId,fragment).commit()
}

