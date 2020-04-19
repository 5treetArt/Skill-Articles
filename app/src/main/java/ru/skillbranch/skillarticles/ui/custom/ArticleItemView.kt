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

    private var dateId: Int? = null
    private var authorId: Int? = null

    private var categoryId: Int? = null

    private var likesId: Int? = null
    private var likesCountId: Int? = null
    private var commentsId: Int? = null
    private var commentsCountId: Int? = null
    private var isBookmarkId: Int? = null

    private val posterSize = context.dpToIntPx(64)
    private val cornerRadius = context.dpToIntPx(8)
    private val iconSize = context.dpToIntPx(16)
    private val categorySize = context.dpToIntPx(40)

    init {
        setPadding(spacingUnit_16)
    }

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
            textSize = 18f
            //setTextSize(/*TypedValue.COMPLEX_UNIT_SP, */18f)
            setTypeface(typeface, Typeface.BOLD)
            text = content.title
            id = R.id.tv_title
        }
        addView(title)

        val poster = ImageView(context).apply {
            //setMarginOptionally(top = marginUnit, bottom = marginUnit)
            id = R.id.iv_poster
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
            id = R.id.tv_description
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
            id = R.id.tv_read_duration
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var usedHeight = paddingTop
        val width = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)

        val date = children.first { it.id == dateId }
        measureChild(date, widthMeasureSpec, heightMeasureSpec)
        val author = children.first { it.id == authorId }
        measureChild(author, widthMeasureSpec, heightMeasureSpec)
        usedHeight += max(date.measuredHeight, author.measuredHeight)

        val title = children.first { it.id == R.id.tv_title }
        val titleWidth =
            width - paddingLeft - paddingRight - posterSize - categorySize / 2 - spacingUnit_4
        val titleWms = MeasureSpec.makeMeasureSpec(titleWidth, MeasureSpec.AT_MOST)
        measureChild(title, titleWms, heightMeasureSpec)
        usedHeight += spacingUnit_8
        usedHeight += max(
            title.measuredHeight + spacingUnit_8,
            posterSize + categorySize / 2
        )

        val description = children.first { it.id == R.id.tv_description }
        measureChild(description, widthMeasureSpec, heightMeasureSpec)
        usedHeight += spacingUnit_8
        usedHeight += description.measuredHeight
        usedHeight += spacingUnit_8

        val likesCount = children.first { it.id == likesCountId }
        measureChild(likesCount, widthMeasureSpec, heightMeasureSpec)
        val commentsCount = children.first { it.id == commentsCountId }
        measureChild(commentsCount, widthMeasureSpec, heightMeasureSpec)
        val readDuration = children.first { it.id == R.id.tv_read_duration }
        measureChild(readDuration, widthMeasureSpec, heightMeasureSpec)

        usedHeight += listOf(
            likesCount.measuredHeight,
            commentsCount.measuredHeight,
            readDuration.measuredHeight,
            iconSize
        ).max() ?: 0

        usedHeight += paddingBottom
        setMeasuredDimension(width, usedHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val bodyWidth = right - left - paddingLeft - paddingRight
        val left = paddingLeft
        val right = paddingLeft + bodyWidth

        val date = children.first { it.id == dateId }
        date.layout(
            left,
            paddingTop,
            left + date.measuredWidth,
            date.measuredHeight + paddingTop
        )
        val author = children.first { it.id == authorId }

        author.layout(
            left + date.measuredWidth + spacingUnit_16,
            paddingTop,
            right,
            paddingTop + author.measuredHeight
        )


        val title = children.first { it.id == R.id.tv_title }
        val barrierTop = paddingTop + max(date.measuredHeight, author.measuredHeight)
        val barrierBottom = barrierTop +
                max(
                    spacingUnit_8 + title.measuredHeight + spacingUnit_8,
                    spacingUnit_8 + posterSize + categorySize / 2
                ) + spacingUnit_8
        val titleTop = barrierTop + (barrierBottom - barrierTop - title.measuredHeight) / 2
        val titleWidth = width - (paddingRight + paddingLeft + posterSize + (categorySize / 2) + context.dpToIntPx(8))
        title.layout(
            left,
            titleTop,
            left + titleWidth, //right - posterSize - (categorySize / 2 + spacingUnit_4),
            titleTop + title.measuredHeight
        )
        val poster = children.first { it.id == R.id.iv_poster }
        val posterTop = barrierTop + (barrierBottom - barrierTop) / 2 - (posterSize + categorySize / 2) / 2
        poster.layout(
            right - posterSize,
            posterTop,
            right,
            posterTop + posterSize
        )
        val category = children.first { it.id == categoryId }
        val categoryTop = posterTop + posterSize - categorySize / 2
        category.layout(
            right - posterSize - categorySize / 2,
            categoryTop,
            right - posterSize + categorySize / 2,
            categoryTop + categorySize
        )

        val description = children.first { it.id == R.id.tv_description }
        description.layout(
            left,
            barrierBottom,
            right,
            barrierBottom + description.measuredHeight
        )
        val descriptionBottom =
            barrierBottom + description.measuredHeight + spacingUnit_8
        val likes = children.first { it.id == likesId }
        likes.layout(
            left,
            descriptionBottom,
            left + iconSize,
            descriptionBottom + iconSize
        )
        val likesCount = children.first { it.id == likesCountId }
        val likesCountLeft = left + iconSize + spacingUnit_8
        likesCount.layout(
            likesCountLeft,
            descriptionBottom,
            likesCountLeft + likesCount.measuredWidth,
            descriptionBottom + likesCount.measuredHeight
        )
        val comments = children.first { it.id == commentsId }
        val commentsLeft = likesCountLeft + likesCount.measuredWidth + spacingUnit_16
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
        val readDuration = children.first { it.id == R.id.tv_read_duration }
        readDuration.layout(
            commentCountLeft + commentsCount.measuredWidth + spacingUnit_16,
            descriptionBottom,
            right - iconSize - spacingUnit_16,
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
