package ru.skillbranch.skillarticles.ui.custom

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.data.ArticleItemData
import ru.skillbranch.skillarticles.extensions.attrValue
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.extensions.format
import kotlin.math.max


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

    private val spacingUnit_4 = context.dpToIntPx(4)
    private val spacingUnit_8 = context.dpToIntPx(8)
    private val spacingUnit_16 = context.dpToIntPx(16)

    private val posterSize = context.dpToIntPx(64)
    private val cornerRadius = context.dpToIntPx(8)
    private val iconSize = context.dpToIntPx(16)
    private val categorySize = context.dpToIntPx(40)

    private val tv_date: TextView
    private val tv_author: TextView
    private val tv_title: TextView
    private val iv_poster: ImageView
    private val iv_category: ImageView
    private val tv_description: TextView
    private val iv_likes: ImageView
    private val tv_likes_count: TextView
    private val iv_comments: ImageView
    private val tv_comments_count: TextView
    private val tv_read_duration: TextView
    private val iv_bookmark: ImageView

    init {
        setPadding(spacingUnit_16)

        tv_date = TextView(context).apply {
            setTextColor(context.getColor(R.color.color_gray))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            //id = ViewCompat.generateViewId()
        }
        addView(tv_date)

        tv_author = TextView(context).apply {
            setTextColor(context.attrValue(R.attr.colorPrimary))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            //id = ViewCompat.generateViewId()
        }
        addView(tv_author)

        tv_title = TextView(context).apply {
            //setMarginOptionally(top = marginUnit, right = marginUnit * 3, bottom = marginUnit)
            setTextColor(context.attrValue(R.attr.colorPrimary))
            textSize = 17.2f
            //setTextSize(/*TypedValue.COMPLEX_UNIT_SP, */18f)
            setTypeface(this.typeface, Typeface.BOLD)
            id = R.id.tv_title
        }
        addView(tv_title)

        iv_poster = ImageView(context).apply {
            //setMarginOptionally(top = marginUnit, bottom = marginUnit)
            id = R.id.iv_poster
        }
        addView(iv_poster)

        iv_category = ImageView(context).apply {
            //id = ViewCompat.generateViewId()
        }
        addView(iv_category)

        tv_description = TextView(context).apply {
            //setMarginOptionally(top = marginUnit)
            setTextColor(context.attrValue(R.attr.colorOnBackground))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            id = R.id.tv_description
        }
        addView(tv_description)

        iv_likes = ImageView(context).apply {
            setImageResource(R.drawable.ic_favorite_black_24dp)
            setColorFilter(
                context.getColor(R.color.color_gray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            //id = ViewCompat.generateViewId()
        }
        addView(iv_likes)

        tv_likes_count = TextView(context).apply {
            //setMarginOptionally(left = marginUnit, top = marginUnit)
            setTextColor(context.getColor(R.color.color_gray))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            //id = ViewCompat.generateViewId()
        }
        addView(tv_likes_count)

        iv_comments = ImageView(context).apply {
            //setMarginOptionally(left = marginUnit * 2)
            setImageResource(R.drawable.ic_insert_comment_black_24dp)
            setColorFilter(
                context.getColor(R.color.color_gray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            //id = ViewCompat.generateViewId()
        }
        addView(iv_comments)

        tv_comments_count = TextView(context).apply {
            //setMarginOptionally(left = marginUnit, top = marginUnit)
            setTextColor(context.getColor(R.color.color_gray))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            //id = ViewCompat.generateViewId()
        }
        addView(tv_comments_count)

        tv_read_duration = TextView(context).apply {
            //setMarginOptionally(left = marginUnit * 2, top = marginUnit, right = marginUnit * 2)
            setTextColor(context.getColor(R.color.color_gray))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            id = R.id.tv_read_duration
        }
        addView(tv_read_duration)

        iv_bookmark = ImageView(context).apply {
            setImageResource(R.drawable.bookmark_states)
            setColorFilter(
                context.getColor(R.color.color_gray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            //id = ViewCompat.generateViewId()
        }
        addView(iv_bookmark)
    }

    fun bind(content: ArticleItemData) {
        tv_date.text = content.date.format()
        tv_author.text = content.author
        tv_title.text = content.title
        tv_description.text = content.description
        tv_likes_count.text = "${content.likeCount}"
        tv_comments_count.text = "${content.commentCount}"
        tv_read_duration.text = "${content.readDuration} min read"

        Glide.with(context)
            .load(content.poster)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .override(posterSize)
            .into(iv_poster)

        Glide.with(context)
            .load(content.categoryIcon)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .override(categorySize)
            .into(iv_category)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var usedHeight = paddingTop
        val width = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)

        measureChild(tv_date, widthMeasureSpec, heightMeasureSpec)
        measureChild(tv_author, widthMeasureSpec, heightMeasureSpec)
        usedHeight += max(tv_date.measuredHeight, tv_author.measuredHeight)

        val titleWidth =
            width - paddingLeft - paddingRight - posterSize - categorySize / 2 - spacingUnit_4
        val titleWms = MeasureSpec.makeMeasureSpec(titleWidth, MeasureSpec.AT_MOST)
       // val titleHms = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        measureChild(tv_title, titleWms, heightMeasureSpec)
        usedHeight += spacingUnit_8
        usedHeight += max(
            tv_title.measuredHeight + spacingUnit_8,
            posterSize + categorySize / 2
        )

        measureChild(tv_description, widthMeasureSpec, heightMeasureSpec)
        usedHeight += spacingUnit_8
        usedHeight += tv_description.measuredHeight
        usedHeight += spacingUnit_8

        measureChild(tv_likes_count, widthMeasureSpec, heightMeasureSpec)
        measureChild(tv_comments_count, widthMeasureSpec, heightMeasureSpec)
        measureChild(tv_read_duration, widthMeasureSpec, heightMeasureSpec)

        usedHeight += listOf(
            tv_likes_count.measuredHeight,
            tv_comments_count.measuredHeight,
            tv_read_duration.measuredHeight,
            iconSize
        ).max() ?: 0

        usedHeight += paddingBottom
        setMeasuredDimension(width, usedHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val bodyWidth = right - left - paddingLeft - paddingRight
        val left = paddingLeft
        val right = paddingLeft + bodyWidth

        tv_date.layout(
            left,
            paddingTop,
            left + tv_date.measuredWidth,
            tv_date.measuredHeight + paddingTop
        )

        tv_author.layout(
            left + tv_date.measuredWidth + spacingUnit_16,
            paddingTop,
            right,
            paddingTop + tv_author.measuredHeight
        )

        val barrierTop = paddingTop + max(tv_date.measuredHeight, tv_author.measuredHeight)
        val barrierBottom = barrierTop +
                max(
                    spacingUnit_8 + tv_title.measuredHeight + spacingUnit_8,
                    spacingUnit_8 + posterSize + categorySize / 2
                ) + spacingUnit_8
        val titleTop = barrierTop + (barrierBottom - barrierTop - tv_title.measuredHeight) / 2
        val titleWidth = width - (paddingRight + paddingLeft + posterSize + (categorySize / 2) + context.dpToIntPx(8))
        tv_title.layout(
            left,
            titleTop,
            left + titleWidth, //right - posterSize - (categorySize / 2 + spacingUnit_4),
            titleTop + tv_title.measuredHeight
        )

        val posterTop = barrierTop + (barrierBottom - barrierTop) / 2 - (posterSize + categorySize / 2) / 2
        iv_poster.layout(
            right - posterSize,
            posterTop,
            right,
            posterTop + posterSize
        )

        val categoryTop = posterTop + posterSize - categorySize / 2
        iv_category.layout(
            right - posterSize - categorySize / 2,
            categoryTop,
            right - posterSize + categorySize / 2,
            categoryTop + categorySize
        )

        tv_description.layout(
            left,
            barrierBottom,
            right,
            barrierBottom + tv_description.measuredHeight
        )
        val descriptionBottom =
            barrierBottom + tv_description.measuredHeight + spacingUnit_8
        iv_likes.layout(
            left,
            descriptionBottom,
            left + iconSize,
            descriptionBottom + iconSize
        )
        val likesCountLeft = left + iconSize + spacingUnit_8
        tv_likes_count.layout(
            likesCountLeft,
            descriptionBottom,
            likesCountLeft + tv_likes_count.measuredWidth,
            descriptionBottom + tv_likes_count.measuredHeight
        )
        val commentsLeft = likesCountLeft + tv_likes_count.measuredWidth + spacingUnit_16
        iv_comments.layout(
            commentsLeft,
            descriptionBottom,
            commentsLeft + iconSize,
            descriptionBottom + iconSize
        )
        val commentCountLeft = commentsLeft + iconSize + spacingUnit_8
        tv_comments_count.layout(
            commentCountLeft,
            descriptionBottom,
            commentCountLeft + tv_comments_count.measuredWidth,
            descriptionBottom + tv_comments_count.measuredHeight
        )
        iv_bookmark.layout(
            right - iconSize,
            descriptionBottom,
            right,
            descriptionBottom + iconSize
        )
        tv_read_duration.layout(
            commentCountLeft + tv_comments_count.measuredWidth + spacingUnit_16,
            descriptionBottom,
            right - iconSize - spacingUnit_16,
            descriptionBottom + tv_read_duration.measuredHeight
        )
    }
}
