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
    var i = -1
    forEach {
        if (it > 0) {
            set(++i, it)
        }
    }
    (i + 1 until size).forEach {
        set(it, -1f)
    }
}

fun FloatArray.add(value: Float) {
    val i = indexOfFirst { it < 0 }
    if (i >= 0) {
        set(i, value)
    }
}