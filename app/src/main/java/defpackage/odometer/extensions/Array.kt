@file:Suppress("unused")

package defpackage.odometer.extensions

fun List<Long>.copyToArray(array: LongArray, placeholder: Long) {
    array.fill(placeholder)
    forEachIndexed { i, value ->
        if (i < array.size) {
            array[i] = value
        } else {
            return
        }
    }
}

fun List<Float>.copyToArray(array: FloatArray, placeholder: Float) {
    array.fill(placeholder)
    forEachIndexed { i, value ->
        if (i < array.size) {
            array[i] = value
        } else {
            return
        }
    }
}