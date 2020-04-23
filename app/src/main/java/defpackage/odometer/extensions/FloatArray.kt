@file:Suppress("unused")

package defpackage.odometer.extensions

fun FloatArray.shiftLeft() {
    (0 until size).forEach {
        if (it < size - 1) {
            set(it, get(it + 1))
        } else {
            set(it, -1f)
        }
    }
}

fun FloatArray.add(value: Float) {
    (0 until size).forEach {
        if (get(it) < 0) {
            set(it, value)
        }
    }
}

fun FloatArray.clear() {
    (0 until size).forEach {
        set(it, -1f)
    }
}