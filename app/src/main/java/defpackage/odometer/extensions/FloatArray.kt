@file:Suppress("unused")

package defpackage.odometer.extensions

inline fun FloatArray.removeAll(predicate: (Float) -> Boolean) {
    (0 until size).forEach {
        if (predicate(get(it))) {
            set(it, -1f)
        }
    }
}

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
    val i = indexOfFirst { it < 0 }
    if (i >= 0) {
        set(i, value)
    }
}