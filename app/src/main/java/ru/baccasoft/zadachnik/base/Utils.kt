package ru.baccasoft.zadachnik.base

import android.content.res.Resources

fun Float.dp(): Float = this * density + 0.5f

fun Float.px(): Float = this * (density / 160)

val density: Float
    get() = Resources.getSystem().displayMetrics.density
