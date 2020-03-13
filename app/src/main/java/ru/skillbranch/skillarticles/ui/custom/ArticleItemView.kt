package ru.skillbranch.skillarticles.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import ru.skillbranch.skillarticles.data.repositories.MarkdownElement
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.extensions.setPaddingOptionally
import ru.skillbranch.skillarticles.ui.custom.markdown.*
import kotlin.properties.Delegates


class ArticleItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    var textSize by Delegates.observable(14f) { _, old, value ->
        if (value == old) return@observable
        this.children.forEach {
            it as IMarkdownView
            it.fontSize = value
        }
    }
    var isLoading: Boolean = true
    val padding = context.dpToIntPx(8)

    init {
        initViews()
    }

    private fun initViews() {
        content.forEach {
            when (it) {
                is MarkdownElement.Text -> {
                    val tv = MarkdownTextView(context, textSize).apply {
                        setPaddingOptionally(
                            left = padding,
                            right = padding
                        )
                        setLineSpacing(fontSize * 0.5f, 1f)
                        id = View.generateViewId()
                        ids.add(id)
                    }

                    MarkdownBuilder(context)
                        .markdownToSpan(it)
                        .run {
                            tv.setText(this, TextView.BufferType.SPANNABLE)
                        }

                    addView(tv)
                }
                is MarkdownElement.Image -> {
                    val iv = MarkdownImageView(
                        context,
                        textSize,
                        it.image.url,
                        it.image.text,
                        it.image.alt
                    ).apply {
                        id = View.generateViewId()
                        ids.add(id)
                    }
                    addView(iv)
                }
                is MarkdownElement.Scroll -> {
                    val sv = MarkdownCodeView(
                        context,
                        textSize,
                        it.blockCode.text
                    ).apply {
                        id = View.generateViewId()
                        ids.add(id)
                    }
                    addView(sv)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var usedHeight = paddingTop
        val width = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        children.forEach {
            measureChild(it, widthMeasureSpec, heightMeasureSpec)
            usedHeight += it.measuredHeight
        }

        usedHeight += paddingBottom
        setMeasuredDimension(width, usedHeight)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var usedHeight = paddingTop
        val bodyWidth = right - left - paddingLeft - paddingRight
        val left = paddingLeft
        val right = paddingLeft + bodyWidth

        children.forEach {
            if (it is MarkdownTextView) {
                it.layout(
                    left - paddingLeft / 2,
                    usedHeight,
                    r - paddingRight / 2,
                    usedHeight + it.measuredHeight
                )
            } else {
                it.layout(
                    left,
                    usedHeight,
                    right,
                    usedHeight + it.measuredHeight
                )
            }
            usedHeight += it.measuredHeight
        }
    }
}