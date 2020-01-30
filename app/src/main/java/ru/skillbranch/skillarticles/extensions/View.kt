package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.*

fun View.setMarginOptionally(
    left: Int = marginLeft,
    top: Int = marginTop,
    right: Int = marginRight,
    bottom: Int = marginBottom
) {
    val currentParams = layoutParams as ViewGroup.MarginLayoutParams
    currentParams.setMargins(left, top, right, bottom)
    layoutParams = currentParams
}

