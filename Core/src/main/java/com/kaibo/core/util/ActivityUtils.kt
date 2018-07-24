package com.kaibo.core.util

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.kaibo.core.R
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult

/**
 * This provides methods to help Activities load their UI.
 */

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 *
 */
fun FragmentActivity.addFragmentToActivity(frameId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().add(frameId, fragment).commit()
}

inline fun <reified T : Activity> Activity.animStartActivity(vararg params: Pair<String, Any?>) {
    startActivity<T>(*params)
    overridePendingTransition(R.anim.translation_right_in, R.anim.translation_right_out)
}

inline fun <reified T : Activity> Activity.animStartActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) {
    startActivityForResult<T>(requestCode, *params)
    overridePendingTransition(R.anim.translation_right_in, R.anim.translation_right_out)
}
