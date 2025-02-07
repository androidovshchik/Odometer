@file:Suppress("unused")

package defpackage.odometer.extension

fun FloatArray.shiftLeft() {
    var i = -1
    forEach {
        if (it > -0.1f) {
            set(++i, it)
        }
    }
    (i + 1 until size).forEach {
        set(it, -1f)
    }
}

fun FloatArray.add(value: Float) {
    val i = indexOfFirst { it < -0.1f }
    if (i >= 0) {
        set(i, value)
    }
}