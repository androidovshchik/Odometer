@file:Suppress("unused")

package defpackage.odometer.extensions

fun FloatArray.shiftLeft(placeholder: Float) {
    (0 until size).forEach {
        if (it < size - 1) {
            set(it, get(it + 1))
        } else {
            set(it, placeholder)
        }
    }
}

fun FloatArray.clear(placeholder: Float) {
    (0 until size).forEach {
        set(it, placeholder)
    }
}