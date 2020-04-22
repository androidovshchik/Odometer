@file:Suppress("unused")

package defpackage.odometer.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager

tailrec fun Context?.activity(): Activity? = when (this) {
    is Activity -> this
    else -> (this as? ContextWrapper)?.baseContext?.activity()
}

inline fun <reified T> Context.activityCallback(action: T.() -> Unit) {
    activity()?.let {
        if (it is T && !it.isFinishing) {
            action(it)
        }
    }
}

fun Context.areGranted(vararg permissions: String): Boolean {
    for (permission in permissions) {
        if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}