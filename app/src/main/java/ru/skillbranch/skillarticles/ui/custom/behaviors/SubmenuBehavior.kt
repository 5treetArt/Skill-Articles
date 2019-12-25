package ru.skillbranch.skillarticles.ui.custom.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.ui.custom.ArticleSubmenu
import ru.skillbranch.skillarticles.ui.custom.Bottombar
import kotlin.math.max
import kotlin.math.min

class SubmenuBehavior(val context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<ArticleSubmenu>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ArticleSubmenu,
        dependency: View
    ): Boolean {
        return dependency is NestedScrollView
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: ArticleSubmenu,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == View.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout, child: ArticleSubmenu, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        if (dy > 0)
            child.animate()
                .translationX(child.width.toFloat() + context.dpToIntPx(8))
                .setDuration(200L)
                .withEndAction { child.visibility = View.INVISIBLE }
                .start()
        else child.animate()
            .translationX(0f)
            .setDuration(200L)
            .withStartAction { child.visibility = View.VISIBLE }
            .start()
        //child.translationX = max(0f, min(child.width.toFloat(), child.translationX + dy))
    }
}