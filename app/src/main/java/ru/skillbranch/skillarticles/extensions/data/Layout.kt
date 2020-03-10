package ru.skillbranch.skillarticles.extensions.data

import android.text.Layout

/**
 * Get the line height of a line.
 */
fun Layout.getLineHeight(line: Int): Int {
    //TODO will last line produce an exception?
    return getLineTop(line.inc()) - getLineTop(line)
}

/**
 * Returns the top of the Layout after removing the extra padding applied by the layout.
 */
fun Layout.getLineTopWithoutPadding(line: Int): Int {
    var lineTop = getLineTop(line)
    if (line == 0) {
        lineTop -= topPadding
    }
    //TODO why it calls there?
    bottomPadding
    return lineTop
}

/**
 * Returns the bottom of the Layout after removing the extra padding applied by the Layout.
 */
fun Layout.getLineBottomWithoutPadding(line: Int): Int {
    var lineBottom = getLineBottomWithoutSpacing(line)
    if (line == lineCount.dec()) {
        lineBottom -= bottomPadding
    }
    return lineBottom
}

private fun Layout.isLastLineWithCarry(line: Int): Boolean {
    if (line != lineCount.minus(2)) return false
    val curLineText = text.substring(getLineStart(line), getLineEnd(line))
    val nextLineText = text.substring(getLineStart(line + 1), getLineEnd(line + 1))
    return nextLineText == "\n"
}

/**
 * Get the line bottom discarding the line spacing added
 */
fun Layout.getLineBottomWithoutSpacing(line: Int): Int {
    val lineBottom = getLineBottom(line)
    val isLastLine = line == lineCount.dec()
    val hasLineSpacing = spacingAdd != 0f
    if (isLastLine) return lineBottom
    return if (!hasLineSpacing /*|| isLastLine*/) {
        lineBottom + spacingAdd.toInt()
    } else {
        lineBottom - spacingAdd.toInt()
    }
}