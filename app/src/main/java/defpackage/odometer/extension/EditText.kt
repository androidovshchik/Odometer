@file:Suppress("unused")

package defpackage.odometer.extension

import android.text.TextWatcher
import android.widget.EditText
import org.jetbrains.anko.sdk19.listeners.__TextWatcher

fun EditText.setTextSelection(text: CharSequence?) {
    (text ?: "").let {
        setText(it)
        setSelection(it.length)
    }
}

inline fun EditText.onTextChanged(init: __TextWatcher.() -> Unit): TextWatcher {
    val listener = __TextWatcher()
    listener.init()
    addTextChangedListener(listener)
    return listener
}