package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
fun View.setMarginOptionally(
    start: Int = marginStart,
    end: Int = marginEnd,
    top: Int = marginTop,
    bottom: Int = marginBottom
) {
    val currentParams = layoutParams as ViewGroup.MarginLayoutParams
    currentParams.setMargins(start, top, end, bottom)
    layoutParams = currentParams
}

