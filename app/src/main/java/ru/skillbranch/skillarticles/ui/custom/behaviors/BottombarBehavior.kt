package ru.skillbranch.skillarticles.ui.custom.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.ui.custom.Bottombar
import kotlin.math.max
import kotlin.math.min

class BottombarBehavior()
    : CoordinatorLayout.Behavior<Bottombar>() {

    constructor(context: Context, attrs: AttributeSet): this()

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: Bottombar,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: Bottombar,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (!child.isSearchMode) {
            val offset = MathUtils.clamp(child.translationY + dy, 0f, child.minHeight.toFloat())
            if (offset != child.translationY) child.translationY = offset
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }
}