package ru.skillbranch.skillarticles.ui.custom.spans

import android.graphics.*
import android.text.style.ReplacementSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.annotation.VisibleForTesting
import ru.skillbranch.skillarticles.data.repositories.Element


class BlockCodeSpan(
    @ColorInt
    private val textColor: Int,
    @ColorInt
    private val bgColor: Int,
    @Px
    private val cornerRadius: Float,
    @Px
    private val padding: Float,
    private val type: Element.BlockCode.Type
) : ReplacementSpan() {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var backgroungRect = RectF() //background rect
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var path = Path()
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var textBoundsRect = Rect() //background rect

    private val startBlockRadius =
        floatArrayOf(
            cornerRadius, cornerRadius, // Top left radius in px
            cornerRadius, cornerRadius, // Top right radius in px
            0f, 0f, // Bottom right radius in px
            0f, 0f // Bottom left radius in px
        )
    private val endBlockRadius = floatArrayOf(
        0f, 0f,
        0f, 0f,
        cornerRadius, cornerRadius,
        cornerRadius, cornerRadius
    )

    companion object {
        const val FONT_SCALE = 0.85f
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        fm ?: return 0
        when (type) {
            Element.BlockCode.Type.SINGLE -> {
                fm.ascent = (paint.ascent() - 2 * padding).toInt()
                fm.descent = (paint.descent() + 2 * padding).toInt()
            }
            Element.BlockCode.Type.START -> {
                fm.ascent = (paint.ascent() - 2 * padding).toInt()
                fm.descent = paint.descent().toInt()
            }
            Element.BlockCode.Type.MIDDLE -> {
                fm.ascent = paint.ascent().toInt()
                fm.descent = paint.descent().toInt()
            }
            Element.BlockCode.Type.END -> {
                fm.ascent = paint.ascent().toInt()
                fm.descent = (paint.descent() + 2 * padding).toInt()
            }
        }
        return 0
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        when (type) {
            Element.BlockCode.Type.SINGLE -> paint.forBackground {
                backgroungRect.set(0f, top + padding, canvas.width.toFloat(), bottom - padding)
                canvas.drawRoundRect(backgroungRect, cornerRadius, cornerRadius, paint)
            }
            Element.BlockCode.Type.START -> paint.forBackground {
                path.reset()
                backgroungRect.set(0f, top + padding, canvas.width.toFloat(), bottom.toFloat())
                path.addRoundRect(backgroungRect, startBlockRadius, Path.Direction.CW)
                canvas.drawPath(path, paint)
            }
            Element.BlockCode.Type.MIDDLE -> paint.forBackground {
                backgroungRect.set(0f, top.toFloat(), canvas.width.toFloat(), bottom.toFloat())
                canvas.drawRect(backgroungRect, paint)
            }
            Element.BlockCode.Type.END -> paint.forBackground {
                path.reset()
                backgroungRect.set(0f, top.toFloat(), canvas.width.toFloat(), bottom - padding)
                path.addRoundRect(backgroungRect, endBlockRadius, Path.Direction.CW)
                canvas.drawPath(path, paint)
            }
        }
        paint.forText {
            canvas.drawText(text, start, end, x + padding, y.toFloat(), paint)
        }
    }

    private inline fun Paint.forText(block: () -> Unit) {
        val oldSize = textSize
        val oldStyle = typeface?.style ?: 0
        val oldFont = typeface
        val oldColor = color

        color = textColor
        typeface = Typeface.create(Typeface.MONOSPACE, oldStyle)
        textSize *= FONT_SCALE

        block()

        color = oldColor
        typeface = oldFont
        textSize = oldSize
    }

    private inline fun Paint.forBackground(block: () -> Unit) {
        val oldColor = color
        val oldStyle = style

        color = bgColor
        style = Paint.Style.FILL

        block()

        color = oldColor
        style = oldStyle
    }

    private fun Canvas.drawFontlines(
        top: Int,
        bottom: Int,
        baseline: Int,
        paint: Paint
    ) {
        drawLine(0f, top + 0f, width + 0f, top + 0f, Paint().apply { color = Color.BLUE })
        drawLine(0f, bottom + 0f, width + 0f, bottom + 0f, Paint().apply { color = Color.GREEN })
        drawLine(0f, baseline + 0f, width + 0f, baseline + 0f, Paint().apply { color = Color.RED })
        drawLine(0f, paint.ascent() + baseline + 0f, width + 0f, paint.ascent() + baseline + 0f, Paint().apply { color = Color.BLACK })
        drawLine(0f, paint.descent() + baseline + 0f, width + 0f, paint.descent() + baseline + 0f, Paint().apply { color = Color.MAGENTA })
    }
}
