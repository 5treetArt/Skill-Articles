package ru.skillbranch.skillarticles.ui.custom

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import com.bumptech.glide.Glide
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.data.ArticleItemData
import ru.skillbranch.skillarticles.extensions.attrValue
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.extensions.format
import ru.skillbranch.skillarticles.extensions.setMarginOptionally
import ru.skillbranch.skillarticles.ui.custom.markdown.*


class ArticleItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    //var textSize by Delegates.observable(14f) { _, old, value ->
    //    if (value == old) return@observable
    //    this.children.forEach {
    //        it as IMarkdownView
    //        it.fontSize = value
    //    }
    //}
    //var isLoading: Boolean = true

    private val marginUnit = context.dpToIntPx(8)

    fun setContent(content: ArticleItemData) {
        val date = TextView(context).apply {
            setTextColor(context.getColor(R.color.color_gray))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            text = content.date.format()
        }
        addView(date)

        val author = TextView(context).apply {
            setMarginOptionally(left = marginUnit * 2)
            setTextColor(context.attrValue(R.attr.colorPrimary))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            text = content.author
        }
        addView(author)

        val title = TextView(context).apply {
            setMarginOptionally(top = marginUnit, right = marginUnit * 3, bottom = marginUnit)
            setTextColor(context.attrValue(R.attr.colorPrimary))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            setTypeface(typeface, Typeface.BOLD)
            text = content.title
        }
        addView(title)

        val poster = ImageView(context).apply {
            setMarginOptionally(top = marginUnit, bottom = marginUnit)
        }

        addView(poster)
        Glide
            .with(context)
            .load(content.poster)
            .transform(AspectRatioResizeTransform())
            .into(poster)

        val category = ImageView(context).apply {
            setImageDrawable(content.category.getDrawable())
        }
        addView(category)

        val description = TextView(context).apply {
            setMarginOptionally(top = marginUnit)
            setTextColor(context.attrValue(R.attr.colorOnBackground))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            text = content.description
        }
        addView(description)

        val likes = ImageView(context).apply {
            setMarginOptionally(top = marginUnit)
            setImageResource(R.drawable.ic_favorite_black_24dp)
        }

        /*
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
        }*/
    }

    private fun String.getDrawable(): Drawable? {
        //TODO add category choise
        context.getDrawable(R.drawable.logo)
        return null
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
