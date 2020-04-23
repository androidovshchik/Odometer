@file:Suppress("unused")

package defpackage.odometer.extensions

fun LongArray.shiftLeft(placeholder: Long) {
    (0 until size).forEach {
        if (it < size - 1) {
            set(it, get(it + 1))
        } else {
            set(it, placeholder)
        }
    }
}

fun LongArray.clear(placeholder: Long) {
    (0 until size).forEach {
        set(it, placeholder)
    }
}