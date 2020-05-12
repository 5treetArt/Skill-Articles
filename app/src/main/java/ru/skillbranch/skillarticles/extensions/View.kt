package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.*
import androidx.navigation.NavDestination
import com.google.android.material.bottomnavigation.BottomNavigationView

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

fun View.setPaddingOptionally(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) = setPadding(left, top, right, bottom)


fun BottomNavigationView.selectDestination(destination: NavDestination) {
    menu.findItem(destination.id)?.isChecked = true
}

fun BottomNavigationView.selectItem(destination: Int?) = destination?.let {
    menu.findItem(it)?.isChecked = true
}