@file:Suppress("unused")

package defpackage.odometer.extensions

fun LongArray.shiftLeft() {
    (0 until size).forEach {
        if (it < size - 1) {
            set(it, get(it + 1))
        } else {
            set(it, -1L)
        }
    }
}

fun LongArray.add(value: Long) {
    (0 until size).forEach {
        if (get(it) < 0) {
            set(it, value)
        }
    }
}

fun LongArray.clear() {
    (0 until size).forEach {
        set(it, -1L)
    }
}