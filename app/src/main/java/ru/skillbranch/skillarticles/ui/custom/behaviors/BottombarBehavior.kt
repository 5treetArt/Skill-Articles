package ru.skillbranch.skillarticles.ui.custom.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.ui.custom.Bottombar
import kotlin.math.max
import kotlin.math.min

class BottombarBehavior(context: Context, attrs: AttributeSet)
    : CoordinatorLayout.Behavior<Bottombar>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: Bottombar,
        dependency: View
    ): Boolean {
        return dependency is NestedScrollView
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: Bottombar,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == View.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout, child: Bottombar, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        if (dy > 0)
            child.animate()
                .translationY(child.height.toFloat())
                .setDuration(200L)
                .withEndAction { child.visibility = View.INVISIBLE }
                .start()
        else child.animate()
            .translationY(0f)
            .setDuration(200L)
            .withStartAction { child.visibility = View.VISIBLE }
            .start()
        //child.translationY = max(0f, min(child.height.toFloat(), child.translationY + dy))
    }
}