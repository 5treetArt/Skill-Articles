package ru.skillbranch.skillarticles.ui.custom

import android.content.Context
import android.content.res.ColorStateList
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
import ru.skillbranch.skillarticles.data.models.ArticleItemData
import ru.skillbranch.skillarticles.extensions.attrValue
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.extensions.format
import kotlin.math.max


class ArticleItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val spacing_8 = context.dpToIntPx(8)
    private val spacing_16 = context.dpToIntPx(16)

    private val posterSize = context.dpToIntPx(64)
    private val cornerRadius = context.dpToIntPx(8)
    private val iconSize = context.dpToIntPx(16)
    private val categorySize = context.dpToIntPx(40)
    private val posterAndCategorySize = posterSize + categorySize / 2
    private val grayColor = context.getColor(R.color.color_gray)
    private val primaryColor = context.attrValue(R.attr.colorPrimary)

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
    private val iv_bookmark: CheckableImageView

    private val smallTextSize = 12f
    private val regularTextSize = 14f
    private val bigTextSize = 18f

    init {
        setPadding(spacing_16)

        tv_date = TextView(context).apply {
            id = R.id.tv_date
            setTextColor(context.getColor(R.color.color_gray))
            textSize = smallTextSize
        }
        addView(tv_date)

        tv_author = TextView(context).apply {
            id = R.id.tv_author
            setTextColor(context.attrValue(R.attr.colorPrimary))
            textSize = smallTextSize
        }
        addView(tv_author)

        tv_title = TextView(context).apply {
            id = R.id.tv_title
            setTextColor(context.attrValue(R.attr.colorPrimary))
            textSize = bigTextSize
            setTypeface(this.typeface, Typeface.BOLD)
        }
        addView(tv_title)

        iv_poster = ImageView(context).apply {
            id = R.id.iv_poster
            layoutParams = LayoutParams(posterSize, posterSize)
        }
        addView(iv_poster)

        iv_category = ImageView(context).apply {
            id = R.id.iv_category
            layoutParams = LayoutParams(categorySize, categorySize)
        }
        addView(iv_category)

        tv_description = TextView(context).apply {
            id = R.id.tv_description
            setTextColor(grayColor)
            textSize = regularTextSize
        }
        addView(tv_description)

        iv_likes = ImageView(context).apply {
            id = R.id.iv_likes
            layoutParams = LayoutParams(iconSize, iconSize)
            imageTintList = ColorStateList.valueOf(grayColor)
            setImageResource(R.drawable.ic_favorite_black_24dp)
        }
        addView(iv_likes)

        tv_likes_count = TextView(context).apply {
            setTextColor(grayColor)
            textSize = smallTextSize
        }
        addView(tv_likes_count)

        iv_comments = ImageView(context).apply {
            imageTintList = ColorStateList.valueOf(grayColor)
            setImageResource(R.drawable.ic_insert_comment_black_24dp)
        }
        addView(iv_comments)

        tv_comments_count = TextView(context).apply {
            id = R.id.tv_comments_count
            setTextColor(grayColor)
            textSize = smallTextSize
        }
        addView(tv_comments_count)

        tv_read_duration = TextView(context).apply {
            id = R.id.tv_read_duration
            setTextColor(grayColor)
            textSize = smallTextSize
        }
        addView(tv_read_duration)

        iv_bookmark = CheckableImageView(context).apply {
            id = R.id.iv_bookmark
            imageTintList = ColorStateList.valueOf(grayColor)
            setImageResource(R.drawable.bookmark_states)
        }
        addView(iv_bookmark)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val titleWidth = width - posterAndCategorySize - spacing_8 //TODO wtf why 8?
        val titleWms = MeasureSpec.makeMeasureSpec(titleWidth, MeasureSpec.AT_MOST)
        //TODO calc author width like "width - (tv_date.measuredWidth + 3 * defaultPadding)"

        measureChild(tv_date, widthMeasureSpec, heightMeasureSpec)
        measureChild(tv_author, widthMeasureSpec, heightMeasureSpec)        //TODO measure author correctly(check width)?
        measureChild(tv_title, titleWms, heightMeasureSpec)
        measureChild(tv_description, widthMeasureSpec, heightMeasureSpec)        //TODO measure description correctly(check width)?
        measureChild(tv_likes_count, widthMeasureSpec, heightMeasureSpec)
        measureChild(tv_comments_count, widthMeasureSpec, heightMeasureSpec)
        measureChild(tv_read_duration, widthMeasureSpec, heightMeasureSpec)        //TODO measure read duration correctly(check width)?

        var usedHeight = paddingTop
        usedHeight += max(tv_date.measuredHeight, tv_author.measuredHeight)
        usedHeight += spacing_8
        usedHeight += max(tv_title.measuredHeight, posterAndCategorySize)
        usedHeight += spacing_8
        usedHeight += tv_description.measuredHeight
        usedHeight += spacing_8
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

        val authorLeft = dateRight + spacing_16
        val authorRight = right
        val authorTop = paddingTop
        val authorBottom = authorTop + tv_author.measuredHeight
        tv_author.layout(authorLeft, authorTop, authorRight, authorBottom)

        val barrierTop = max(dateBottom, authorBottom)
        val barrierBottom = barrierTop +
                spacing_8 +
                max(tv_title.measuredHeight, posterAndCategorySize) +
                spacing_8
        val centerBetweenBarriers = barrierTop + (barrierBottom - barrierTop) / 2

        val titleLeft = left
        val titleRight = titleLeft + tv_title.measuredWidth
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

        val descriptionBottomWithSpacing = descriptionBottom + spacing_8

        val likesLeft = left
        val likesRight = likesLeft + iconSize
        val likesTop = descriptionBottomWithSpacing
        val likesBottom = likesTop + iconSize
        iv_likes.layout(likesLeft, likesTop, likesRight, likesBottom)

        val likesCountLeft = likesRight + spacing_8
        val likesCountRight = likesCountLeft + tv_likes_count.measuredWidth
        val likesCountTop = descriptionBottomWithSpacing
        val likesCountBottom = likesCountTop + tv_likes_count.measuredHeight
        tv_likes_count.layout(likesCountLeft, likesCountTop, likesCountRight, likesCountBottom)

        val commentsLeft = likesCountRight + spacing_16
        val commentsRight = commentsLeft + iconSize
        val commentsTop = descriptionBottomWithSpacing
        val commentsBottom = commentsTop + iconSize
        iv_comments.layout(commentsLeft, commentsTop, commentsRight, commentsBottom)

        val commentCountLeft = commentsRight + spacing_8
        val commentsCountRight = commentCountLeft + tv_comments_count.measuredWidth
        val commentsCountTop = descriptionBottomWithSpacing
        val commentsCountBottom = commentsCountTop + tv_comments_count.measuredHeight
        tv_comments_count.layout(commentCountLeft, commentsCountTop, commentsCountRight, commentsCountBottom)

        val bookmarkLeft = right - iconSize
        val bookmarkRight = right
        val bookmarkTop = descriptionBottomWithSpacing
        val bookmarkBottom = bookmarkTop + iconSize
        iv_bookmark.layout(bookmarkLeft, bookmarkTop, bookmarkRight, bookmarkBottom)

        val readDurationLeft = commentsCountRight + spacing_16
        val readDurationRight = bookmarkLeft - spacing_16
        val readDurationTop = descriptionBottomWithSpacing
        val readDurationBottom = readDurationTop + tv_read_duration.measuredHeight
        tv_read_duration.layout(readDurationLeft, readDurationTop, readDurationRight, readDurationBottom)


/*        var usedHeight = paddingTop
        val bodyWidth = right - left - paddingLeft - paddingRight
        var left = paddingLeft

        tv_date.layout(
            left,
            usedHeight,
            left + tv_date.measuredWidth,
            usedHeight + tv_date.measuredHeight
        )
        left = tv_date.right + defaultPadding
        tv_author.layout(
            left,
            usedHeight,
            left + tv_author.measuredWidth,
            usedHeight + tv_author.measuredHeight
        )
        usedHeight += tv_author.measuredHeight + defaultSpace
        left = paddingLeft

        val rh = posterSize + categorySize / 2
        val leftTop = if (rh > tv_title.measuredHeight) (rh - tv_title.measuredHeight) / 2 else 0
        val rightTop = if (rh < tv_title.measuredHeight) (tv_title.measuredHeight - rh) / 2 else 0

        tv_title.layout(
            left,
            usedHeight + leftTop,
            left + tv_title.measuredWidth,
            usedHeight + leftTop + tv_title.measuredHeight
        )
        iv_poster.layout(
            left + bodyWidth - posterSize,
            usedHeight + rightTop,
            left + bodyWidth,
            usedHeight + rightTop + posterSize
        )
        iv_category.layout(
            iv_poster.left - categorySize / 2,
            iv_poster.bottom - categorySize / 2,
            iv_poster.left + categorySize / 2,
            iv_poster.bottom + categorySize / 2
        )
        usedHeight += if (rh > tv_title.measuredHeight) rh else tv_title.measuredHeight
        usedHeight += defaultSpace

        tv_description.layout(
            left,
            usedHeight,
            left + bodyWidth,
            usedHeight + tv_description.measuredHeight
        )
        usedHeight += tv_description.measuredHeight + defaultSpace

        val fontDiff = iconSize - tv_likes_count.measuredHeight
        iv_likes.layout(
            left,
            usedHeight - fontDiff,
            left + iconSize,
            usedHeight + iconSize - fontDiff
        )

        left = iv_likes.right + defaultSpace
        tv_likes_count.layout(
            left,
            usedHeight,
            left + tv_likes_count.measuredWidth,
            usedHeight + tv_likes_count.measuredHeight
        )
        left = tv_likes_count.right + defaultPadding

        iv_comments.layout(
            left,
            usedHeight - fontDiff,
            left + iconSize,
            usedHeight + iconSize - fontDiff
        )
        left = iv_comments.right + defaultSpace
        tv_comments_count.layout(
            left,
            usedHeight,
            left + tv_comments_count.measuredWidth,
            usedHeight + tv_comments_count.measuredHeight
        )
        left = tv_comments_count.right + defaultPadding
        tv_read_duration.layout(
            left,
            usedHeight,
            left + tv_read_duration.measuredWidth,
            usedHeight + tv_read_duration.measuredHeight
        )

        left = defaultPadding
        iv_bookmark.layout(
            left + bodyWidth - iconSize,
            usedHeight - fontDiff,
            left + bodyWidth,
            usedHeight + iconSize - fontDiff
        )*/
    }

    fun bind(item: ArticleItemData, toggleBookmarkListener: (articleId: String, isBookmark: Boolean) -> Unit) {
        tv_date.text = item.date.format()
        tv_author.text = item.author
        tv_title.text = item.title

        Glide.with(context)
            .load(item.poster)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .override(posterSize)
            .into(iv_poster)

        Glide.with(context)
            .load(item.categoryIcon)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .override(categorySize)
            .into(iv_category)

        tv_description.text = item.description
        tv_likes_count.text = "${item.likeCount}"
        tv_comments_count.text = "${item.commentCount}"
        tv_read_duration.text = "${item.readDuration} min read"
        iv_bookmark.isChecked = item.isBookmark
        iv_bookmark.setOnClickListener { toggleBookmarkListener.invoke(item.id, !item.isBookmark) }
    }
}