package ru.skillbranch.skillarticles.ui.custom

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

    private val spacingUnit_4 = context.dpToIntPx(4)
    private val spacingUnit_8 = context.dpToIntPx(8)
    private val spacingUnit_16 = context.dpToIntPx(16)

    private val posterSize = context.dpToIntPx(64)
    private val cornerRadius = context.dpToIntPx(8)
    private val iconSize = context.dpToIntPx(16)
    private val categorySize = context.dpToIntPx(40)
    private val posterAndCategorySize = posterSize + categorySize / 2

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

    private val smallTextSize = 12f
    private val regularTextSize = 14f
    private val bigTextSize = 18f

    init {
        setPadding(spacingUnit_16)

        tv_date = TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setTextColor(context.getColor(R.color.color_gray))
            textSize = smallTextSize
        }
        addView(tv_date)

        tv_author = TextView(context).apply {
            id = R.id.tv_author
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setTextColor(context.attrValue(R.attr.colorPrimary))
            textSize = smallTextSize
        }
        addView(tv_author)

        tv_title = TextView(context).apply {
            id = R.id.tv_title
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setTextColor(context.attrValue(R.attr.colorPrimary))
            textSize = bigTextSize
            typeface = Typeface.DEFAULT_BOLD
            //setTypeface(this.typeface, Typeface.BOLD)
        }
        addView(tv_title)

        iv_poster = ImageView(context).apply {
            id = R.id.iv_poster
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        }
        addView(iv_poster)

        iv_category = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        }
        addView(iv_category)

        tv_description = TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setTextColor(context.attrValue(R.attr.colorOnBackground))
            textSize = regularTextSize
            id = R.id.tv_description
        }
        addView(tv_description)

        iv_likes = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setImageResource(R.drawable.ic_favorite_black_24dp)
            setColorFilter(
                context.getColor(R.color.color_gray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        addView(iv_likes)

        tv_likes_count = TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setTextColor(context.getColor(R.color.color_gray))
            textSize = smallTextSize
        }
        addView(tv_likes_count)

        iv_comments = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setImageResource(R.drawable.ic_insert_comment_black_24dp)
            setColorFilter(
                context.getColor(R.color.color_gray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        addView(iv_comments)

        tv_comments_count = TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setTextColor(context.getColor(R.color.color_gray))
            textSize = smallTextSize
        }
        addView(tv_comments_count)

        tv_read_duration = TextView(context).apply {
            id = R.id.tv_read_duration
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setTextColor(context.getColor(R.color.color_gray))
            textSize = smallTextSize
        }
        addView(tv_read_duration)

        iv_bookmark = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setImageResource(R.drawable.bookmark_states)
            setColorFilter(
                context.getColor(R.color.color_gray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        addView(iv_bookmark)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val bodyWidth = right - left - paddingLeft - paddingRight
        val left = paddingLeft
        val right = paddingLeft + bodyWidth

        var usedHeight = paddingTop
        val width = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)

        measureChild(tv_date, widthMeasureSpec, heightMeasureSpec)
        measureChild(tv_author, widthMeasureSpec, heightMeasureSpec)
        usedHeight += max(tv_date.measuredHeight, tv_author.measuredHeight)

        val titleWidth = bodyWidth/*width - paddingLeft - paddingRight*/ - posterAndCategorySize - spacingUnit_8
        val titleWms = MeasureSpec.makeMeasureSpec(titleWidth, MeasureSpec.AT_MOST)
        // val titleHms = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        measureChild(tv_title, titleWms, heightMeasureSpec)
        usedHeight += spacingUnit_8
        usedHeight += spacingUnit_8 //TODO remove
        usedHeight += max(
            tv_title.measuredHeight/* + spacingUnit_8*/,
            posterAndCategorySize
        )
        usedHeight += spacingUnit_8

        measureChild(tv_description, widthMeasureSpec, heightMeasureSpec)
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

        val dateLeft = left
        val dateRight = dateLeft + tv_date.measuredWidth
        val dateTop = paddingTop
        val dateBottom = dateTop + tv_date.measuredHeight
        tv_date.layout(dateLeft, dateTop, dateRight, dateBottom)

        val authorLeft = dateRight + spacingUnit_16
        val authorRight = right
        val authorTop = paddingTop
        val authorBottom = authorTop + tv_author.measuredHeight
        tv_author.layout(authorLeft, authorTop, authorRight, authorBottom)

        val barrierTop = max(dateBottom, authorBottom)
        val barrierBottom = barrierTop +
                spacingUnit_8 +
                max(
                    tv_title.measuredHeight,// + spacingUnit_8,
                    posterAndCategorySize
                ) +
                spacingUnit_8
        val centerBetweenBarriers = barrierTop + (barrierBottom - barrierTop) / 2

        //val titleWidth = width - paddingLeft - paddingRight - posterAndCategorySize - spacingUnit_8 //TODO wtf why 8?
        val titleLeft = left
        val titleRight = /*titleLeft + titleWidth*/ right - posterAndCategorySize - spacingUnit_8
        val titleTop = centerBetweenBarriers - tv_title.measuredHeight / 2
        val titleBottom = titleTop + tv_title.measuredHeight
        tv_title.layout(titleLeft, titleTop, titleRight, titleBottom)

        val posterLeft = right - posterSize
        val posterRight = right
        val posterTop = centerBetweenBarriers - posterAndCategorySize / 2
        val posterBottom = posterTop + posterSize
        iv_poster.layout(posterLeft, posterTop, posterRight, posterBottom)

        val categoryLeft = posterLeft - categorySize / 2
        val categoryRight = categoryLeft + categorySize
        val categoryTop = posterBottom - categorySize / 2
        val categoryBottom = categoryTop + categorySize
        iv_category.layout(categoryLeft, categoryTop, categoryRight, categoryBottom)

        val descriptionLeft = left
        val descriptionRight = right
        val descriptionTop = barrierBottom
        val descriptionBottom = barrierBottom + tv_description.measuredHeight
        tv_description.layout(descriptionLeft, descriptionTop, descriptionRight, descriptionBottom)

        val descriptionBottomWithSpacing = barrierBottom + tv_description.measuredHeight + spacingUnit_8

        val likesLeft = left
        val likesRight = likesLeft + iconSize
        val likesTop = descriptionBottomWithSpacing
        val likesBottom = likesTop + iconSize
        iv_likes.layout(likesLeft, likesTop, likesRight, likesBottom)

        val likesCountLeft = likesRight + spacingUnit_8
        val likesCountRight = likesCountLeft + tv_likes_count.measuredWidth
        val likesCountTop = descriptionBottomWithSpacing
        val likesCountBottom = likesCountTop + tv_likes_count.measuredHeight
        tv_likes_count.layout(likesCountLeft, likesCountTop, likesCountRight, likesCountBottom)

        val commentsLeft = likesCountRight + spacingUnit_16
        val commentsRight = commentsLeft + iconSize
        val commentsTop = descriptionBottomWithSpacing
        val commentsBottom = commentsTop + iconSize
        iv_comments.layout(commentsLeft, commentsTop, commentsRight, commentsBottom)

        val commentCountLeft = commentsRight + spacingUnit_8
        val commentsCountRight = commentCountLeft + tv_comments_count.measuredWidth
        val commentsCountTop = descriptionBottomWithSpacing
        val commentsCountBottom = commentsCountTop + tv_comments_count.measuredHeight
        tv_comments_count.layout(commentCountLeft, commentsCountTop, commentsCountRight, commentsCountBottom)

        val bookmarkLeft = right - iconSize
        val bookmarkRight = right
        val bookmarkTop = descriptionBottomWithSpacing
        val bookmarkBottom = bookmarkTop + iconSize
        iv_bookmark.layout(bookmarkLeft, bookmarkTop, bookmarkRight, bookmarkBottom)

        val readDurationLeft = commentsCountRight + spacingUnit_16
        val readDurationRight = bookmarkLeft - spacingUnit_16
        val readDurationTop = descriptionBottomWithSpacing
        val readDurationBottom = readDurationTop + tv_read_duration.measuredHeight
        tv_read_duration.layout(readDurationLeft, readDurationTop, readDurationRight, readDurationBottom)
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
}