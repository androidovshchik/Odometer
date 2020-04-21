@file:Suppress("unused")

package defpackage.odometer.extensions

import android.widget.EditText

fun EditText.setTextSelection(text: CharSequence?) {
    (text ?: "").let {
        setText(it)
        setSelection(it.length)
    }
}