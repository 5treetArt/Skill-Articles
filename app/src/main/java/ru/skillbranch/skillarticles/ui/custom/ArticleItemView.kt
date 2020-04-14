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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.data.ArticleItemData
import ru.skillbranch.skillarticles.extensions.attrValue
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.extensions.format
import ru.skillbranch.skillarticles.ui.custom.markdown.*
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

    private val spacingUnit_8 = context.dpToIntPx(8)

    private var dateId: Int? = null
    private var titleId: Int? = null
    private var authorId: Int? = null
    private var posterId: Int? = null
    private var categoryId: Int? = null
    private var descriptionId: Int? = null
    private var likesId: Int? = null
    private var likesCountId: Int? = null
    private var commentsId: Int? = null
    private var commentsCountId: Int? = null
    private var readDurationId: Int? = null
    private var isBookmarkId: Int? = null

    private val posterSize = context.dpToIntPx(64)
    private val cornerRadius = context.dpToIntPx(8)
    private val iconSize = context.dpToIntPx(16)
    private val categorySize = context.dpToIntPx(40)

    fun bind(content: ArticleItemData) {
        val date = TextView(context).apply {
            setTextColor(context.getColor(R.color.color_gray))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            text = content.date.format()
            id = ViewCompat.generateViewId()
            dateId = id
        }
        addView(date)

        val author = TextView(context).apply {
            //setMarginOptionally(left = marginUnit * 2)
            setTextColor(context.attrValue(R.attr.colorPrimary))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            text = content.author
            id = ViewCompat.generateViewId()
            authorId = id
        }
        addView(author)

        val title = TextView(context).apply {
            //setMarginOptionally(top = marginUnit, right = marginUnit * 3, bottom = marginUnit)
            setTextColor(context.attrValue(R.attr.colorPrimary))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            setTypeface(typeface, Typeface.BOLD)
            text = content.title
            id = ViewCompat.generateViewId()
            titleId = id
        }
        addView(title)

        val poster = ImageView(context).apply {
            //setMarginOptionally(top = marginUnit, bottom = marginUnit)
            id = ViewCompat.generateViewId()
            posterId = id
        }
        addView(poster)

        Glide.with(context)
            .load(content.poster)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .override(posterSize)
            .into(poster)

        val category = ImageView(context).apply {
            id = ViewCompat.generateViewId()
            categoryId = id
        }
        addView(category)

        Glide.with(context)
            .load(content.categoryIcon)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .override(categorySize)
            .into(category)

        val description = TextView(context).apply {
            //setMarginOptionally(top = marginUnit)
            setTextColor(context.attrValue(R.attr.colorOnBackground))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            text = content.description
            id = ViewCompat.generateViewId()
            descriptionId = id
        }
        addView(description)

        val likes = ImageView(context).apply {
            setImageResource(R.drawable.ic_favorite_black_24dp)
            setColorFilter(
                context.getColor(R.color.color_gray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            id = ViewCompat.generateViewId()
            likesId = id
        }
        addView(likes)

        val likesCount = TextView(context).apply {
            //setMarginOptionally(left = marginUnit, top = marginUnit)
            setTextColor(context.getColor(R.color.color_gray))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            text = "${content.likeCount}"
            id = ViewCompat.generateViewId()
            likesCountId = id
        }
        addView(likesCount)

        val comments = ImageView(context).apply {
            //setMarginOptionally(left = marginUnit * 2)
            setImageResource(R.drawable.ic_insert_comment_black_24dp)
            setColorFilter(
                context.getColor(R.color.color_gray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            id = ViewCompat.generateViewId()
            commentsId = id
        }
        addView(comments)

        val commentsCount = TextView(context).apply {
            //setMarginOptionally(left = marginUnit, top = marginUnit)
            setTextColor(context.getColor(R.color.color_gray))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            text = "${content.commentCount}"
            id = ViewCompat.generateViewId()
            commentsCountId = id
        }
        addView(commentsCount)

        val readDuration = TextView(context).apply {
            //setMarginOptionally(left = marginUnit * 2, top = marginUnit, right = marginUnit * 2)
            setTextColor(context.getColor(R.color.color_gray))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            text = "${content.readDuration} min read"
            id = ViewCompat.generateViewId()
            readDurationId = id
        }
        addView(readDuration)

        val isBookmark = ImageView(context).apply {
            setImageResource(R.drawable.bookmark_states)
            setColorFilter(
                context.getColor(R.color.color_gray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            id = ViewCompat.generateViewId()
            isBookmarkId = id
        }
        addView(isBookmark)
    }

    //private fun String.getDrawable(): Drawable? {
    //    //TODO add category choise
    //    context.getDrawable(R.drawable.logo)
    //    return null
    //}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var usedHeight = paddingTop
        //var usedWidth = paddingLeft
        val width = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)

        val date = children.first { it.id == dateId }
        measureChild(date, MeasureSpec.EXACTLY, MeasureSpec.EXACTLY)
        val author = children.first { it.id == authorId }
        measureChild(author, MeasureSpec.EXACTLY, MeasureSpec.EXACTLY)
        usedHeight += max(date.measuredHeight, author.measuredHeight)

        val title = children.first { it.id == titleId }
        measureChild(title, MeasureSpec.AT_MOST, MeasureSpec.EXACTLY)
        //val poster = children.first { it.id == posterId }
        //measureChild(poster, MeasureSpec.EXACTLY, MeasureSpec.EXACTLY)
        //val category = children.first { it.id == categoryId }
        //measureChild(category, MeasureSpec.EXACTLY, MeasureSpec.EXACTLY)
        usedHeight += max(title.measuredHeight, posterSize + categorySize / 2)

        val description = children.first { it.id == descriptionId }
        measureChild(date, MeasureSpec.AT_MOST, MeasureSpec.EXACTLY)
        usedHeight += description.measuredHeight

        //val likes = children.first { it.id == likesId }
        //measureChild(likes, MeasureSpec.EXACTLY, MeasureSpec.EXACTLY)
        val likesCount = children.first { it.id == likesCountId }
        measureChild(likesCount, MeasureSpec.EXACTLY, MeasureSpec.EXACTLY)
        //val comments = children.first { it.id == commentsId }
        //measureChild(comments, MeasureSpec.EXACTLY, MeasureSpec.EXACTLY)
        val commentsCount = children.first { it.id == commentsCountId }
        measureChild(commentsCount, MeasureSpec.EXACTLY, MeasureSpec.EXACTLY)
        val readDuration = children.first { it.id == readDurationId }
        measureChild(readDuration, MeasureSpec.AT_MOST, MeasureSpec.EXACTLY)
        //val isBookmark = children.first { it.id == isBookmarkId }
        //measureChild(isBookmark, MeasureSpec.EXACTLY, MeasureSpec.EXACTLY)

        usedHeight += listOf(
            //likes.measuredHeight,
            likesCount.measuredHeight,
            //comments.measuredHeight,
            commentsCount.measuredHeight,
            readDuration.measuredHeight,
            iconSize
            //isBookmark.measuredHeight
        ).max() ?: 0

        //children.forEach {
        //    measureChild(it, widthMeasureSpec, heightMeasureSpec)
        //    usedHeight += it.measuredHeight
        //    //usedWidth += it.measuredHeight
        //}

        usedHeight += paddingBottom
        //usedWidth += paddingRight
        setMeasuredDimension(width, usedHeight)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val bodyWidth = right - left - paddingLeft - paddingRight
        val left = paddingLeft
        val right = paddingLeft + bodyWidth

        val date = children.first { it.id == dateId }
        date.layout(
            left - paddingLeft,
            paddingTop,
            date.measuredWidth,
            date.measuredHeight + paddingTop
        )
        val author = children.first { it.id == authorId }
        author.layout(
            date.measuredWidth,
            paddingTop,
            date.measuredWidth + 2 * spacingUnit_8 + author.measuredWidth,
            author.measuredHeight + paddingTop
        )

        val titleTop = max(date.measuredHeight, author.measuredHeight) + spacingUnit_8
        val poster = children.first { it.id == posterId }
        poster.layout(
            right - posterSize,
            titleTop,
            right,
            titleTop + posterSize
        )
        val category = children.first { it.id == categoryId }
        category.layout(
            right - posterSize - categorySize / 2,
            titleTop + posterSize - categorySize / 2,
            right - posterSize + categorySize / 2,
            titleTop + posterSize + categorySize / 2
        )
        val title = children.first { it.id == titleId }
        title.layout(
            left - paddingLeft,
            titleTop,
            right - posterSize - categorySize / 2 - spacingUnit_8 * 3,
            titleTop + title.measuredHeight
        )

        val descriptionTop =
            titleTop + max(title.measuredHeight, posterSize + categorySize / 2) + spacingUnit_8
        val description = children.first { it.id == descriptionId }
        description.layout(
            left - paddingLeft,
            descriptionTop,
            right,
            description.measuredHeight
        )
        val descriptionBottom = descriptionTop + description.measuredHeight + spacingUnit_8
        val likes = children.first { it.id == likesId }
        likes.layout(
            left - paddingLeft,
            descriptionBottom,
            left - paddingLeft + iconSize,
            descriptionBottom + iconSize
        )
        val likesCount = children.first { it.id == likesCountId }
        val likesCountLeft = left - paddingLeft + iconSize + spacingUnit_8
        likesCount.layout(
            likesCountLeft,
            descriptionBottom,
            likesCountLeft + likesCount.measuredWidth,
            descriptionBottom + likesCount.measuredHeight
        )
        val comments = children.first { it.id == commentsId }
        val commentsLeft = likesCountLeft + likesCount.measuredWidth + spacingUnit_8 * 2
        comments.layout(
            commentsLeft,
            descriptionBottom,
            commentsLeft + iconSize,
            descriptionBottom + iconSize
        )
        val commentsCount = children.first { it.id == commentsCountId }
        val commentCountLeft = commentsLeft + iconSize + spacingUnit_8
        commentsCount.layout(
            commentCountLeft,
            descriptionBottom,
            commentCountLeft + commentsCount.measuredWidth,
            descriptionBottom + commentsCount.measuredHeight
        )
        val isBookmark = children.first { it.id == isBookmarkId }
        isBookmark.layout(
            right - iconSize,
            descriptionBottom,
            right,
            descriptionBottom + iconSize
        )
        val readDuration = children.first { it.id == readDurationId }
        readDuration.layout(
            commentCountLeft + commentsCount.measuredWidth + spacingUnit_8 * 2,
            descriptionBottom,
            right - iconSize - spacingUnit_8 * 2,
            descriptionBottom + readDuration.measuredHeight
        )
        //children.forEach {
        //    if (it is MarkdownTextView) {
        //        it.layout(
        //            left - paddingLeft / 2,
        //            usedHeight,
        //            r - paddingRight / 2,
        //            usedHeight + it.measuredHeight
        //        )
        //    } else {
        //        it.layout(
        //            left,
        //            usedHeight,
        //            right,
        //            usedHeight + it.measuredHeight
        //        )
        //    }
        //    usedHeight += it.measuredHeight
        //}
    }
}
