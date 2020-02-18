package ru.skillbranch.skillarticles.markdown

import java.util.regex.Pattern
import kotlin.math.E

object MarkdownParser {

    private val LINE_SEPARATOR = System.getProperty("line.separator") ?: "\n"

    //group regex
    private const val UNORDERED_LIST_ITEM_GROUP = "(^[*+-] .+$)"
    private const val HEADER_GROUP = "(^#{1,6} .+?$)"
    private const val QUOTE_GROUP = "(^> .+?$)"

    private const val ITALIC_GROUP = "((?<!\\*)\\*[^*].*?[^*]?\\*(?!\\*)|(?<!_)_[^_].*?[^_]?_(?!_))"
    private const val BOLD_GROUP =
        "((?<!\\*)\\*{2}[^*].*?[^*]?\\*{2}(?!\\*)|(?<!_)_{2}[^_].*?[^_]?_{2}(?!_))"
    private const val STRIKE_GROUP = "(~~.+?~~)"

    private const val RULE_GROUP = "(^[-_*]{3}$)"
    private const val INLINE_GROUP = "((?<!`)`[^`\\s].*?[^`\\s]?`(?!`))"
    private const val LINK_GROUP = "(\\[[^\\[\\]]*?]\\(.+?\\)|^\\[*?]\\(.*?\\))"

    private const val ORDERED_LIST_ITEM_GROUP = "(^\\d+\\. .+$)"
    private const val BLOCK_GROUP = "(^(?<!`)`{3}[^`]+?`{3}(?!`)$)"

    private const val GROUP_COUNT = 11

    //result regex
    private const val MARKDOWN_GROUPS = "$UNORDERED_LIST_ITEM_GROUP|$HEADER_GROUP|$QUOTE_GROUP" +
            "|$ITALIC_GROUP|$BOLD_GROUP|$STRIKE_GROUP|$RULE_GROUP|$INLINE_GROUP|$LINK_GROUP" +
            "|$ORDERED_LIST_ITEM_GROUP|$BLOCK_GROUP"

    private val elementsPattern by lazy { Pattern.compile(MARKDOWN_GROUPS, Pattern.MULTILINE) }

    /**
     * parse markdown text to elements
     */
    fun parse(string: String): MarkdownText {
        val elements = mutableListOf<Element>()
        elements.addAll(findElements(string))
        return MarkdownText(elements)
    }

    /**
     * clear markdown text to string without markdown characters
     */
    fun clear(string: String?): String? {
        string ?: return null

        val result = StringBuilder()
        val parents = mutableListOf<Element>()
        val matcher = elementsPattern.matcher(string)

        var lastStartIndex = 0

        loop@ while (matcher.find(lastStartIndex)) {
            val startIndex = matcher.start()
            val endIndex = matcher.end()

            //if something is found then everything before - TEXT
            if (lastStartIndex < startIndex) {
                result.append(string.subSequence(lastStartIndex, startIndex))
            }

            //found text
            var text: String

            //groups range for iterate by groups
            val groups = 1..GROUP_COUNT
            var group = -1
            for (gr in groups) {
                if (matcher.group(gr) != null) {
                    group = gr
                    break
                }
            }

            when (group) {
                //NOT FOUND -> BREAK
                -1 -> break@loop

                //UNORDERED LIST
                1 -> {
                    //text without "* "
                    text = string.substring(startIndex.plus(2), endIndex)

                    //find inner elements
                    val subs = clear(text)
                    result.append(subs)
                    //next find start from position "endIndex" (last regex character)
                    lastStartIndex = endIndex
                }

                //HEADER
                2 -> {
                    val reg = "^#{1,6}".toRegex().find(string.subSequence(startIndex, endIndex))
                    val level = reg!!.value.length

                    //text without "{#} "
                    text = string.substring(startIndex.plus(level.inc()), endIndex)
                    result.append(text)
                    lastStartIndex = endIndex
                }

                //QUOTES
                3 -> {
                    //text without "> "
                    text = string.substring(startIndex.plus(2), endIndex)
                    val subs = clear(text)
                    result.append(subs)
                    lastStartIndex = endIndex
                }

                //ITALIC
                4 -> {
                    //text without "*{}*"
                    text = string.substring(startIndex.inc(), endIndex.dec())
                    val subs = clear(text)
                    result.append(subs)
                    lastStartIndex = endIndex
                }

                //BOLD
                5 -> {
                    //text without "**{}**"
                    text = string.substring(startIndex.plus(2), endIndex.minus(2))
                    val subs = clear(text)
                    result.append(subs)
                    lastStartIndex = endIndex
                }

                //STRIKE
                6 -> {
                    //text without "~~{}~~"
                    text = string.substring(startIndex.plus(2), endIndex.minus(2))
                    val subs = clear(text)
                    result.append(subs)
                    lastStartIndex = endIndex
                }

                //RULE
                7 -> {
                    text = " ".plus(string.substring(startIndex.plus(3), endIndex))
                    result.append(text)
                    lastStartIndex = endIndex
                }

                //INLINE CODE
                8 -> {
                    //text without "`{}`"
                    text = string.substring(startIndex.inc(), endIndex.dec())
                    result.append(text)
                    lastStartIndex = endIndex
                }

                //LINK
                9 -> {
                    //full text for regex
                    text = string.substring(startIndex, endIndex)
                    val (title: String, link: String) = "\\[(.*)]\\((.*)\\)".toRegex().find(text)!!.destructured
                    result.append(title)
                    lastStartIndex = endIndex
                }

                //ORDERED LIST
                10 -> {
                    //full text for regex
                    val preHandledText = string.substring(startIndex, endIndex)
                    //text without "\d+. "
                    text = "^\\d+\\. (.+)\$".toRegex().find(preHandledText)!!.destructured.component1()
                    //find inner elements
                    val subs = clear(text)
                    result.append(subs)
                    //next find start from position "endIndex" (last regex character)
                    lastStartIndex = endIndex
                }

                //BLOCK CODE
                11 -> {
                    //text without "```{}```"
                    text = string.substring(startIndex.plus(3), endIndex.minus(3))
                    result.append(text)
                    lastStartIndex = endIndex
                }
            }
        }

        if (lastStartIndex < string.length) {
            val text = string.subSequence(lastStartIndex, string.length)
            result.append(text)
        }

        return result.toString()
    }

    /**
     * find markdown elements in markdown text
     */
    private fun findElements(string: CharSequence): List<Element> {
        val parents = mutableListOf<Element>()
        val matcher = elementsPattern.matcher(string)

        var lastStartIndex = 0

        loop@ while (matcher.find(lastStartIndex)) {
            val startIndex = matcher.start()
            val endIndex = matcher.end()

            //if something is found then everything before - TEXT
            if (lastStartIndex < startIndex) {
                parents.add(Element.Text(string.subSequence(lastStartIndex, startIndex)))
            }

            //found text
            var text: CharSequence

            //groups range for iterate by groups
            val groups = 1..GROUP_COUNT
            var group = -1
            for (gr in groups) {
                if (matcher.group(gr) != null) {
                    group = gr
                    break
                }
            }

            when (group) {
                //NOT FOUND -> BREAK
                -1 -> break@loop

                //UNORDERED LIST
                1 -> {
                    //text without "*. "
                    text = string.subSequence(startIndex.plus(2), endIndex)

                    //find inner elements
                    val subs = findElements(text)
                    val element = Element.UnorderedListItem(text, subs)
                    parents.add(element)

                    //next find start from position "endIndex" (last regex character)
                    lastStartIndex = endIndex
                }

                //HEADER
                2 -> {
                    val reg = "^#{1,6}".toRegex().find(string.subSequence(startIndex, endIndex))
                    val level = reg!!.value.length

                    //text without "{#} "
                    text = string.subSequence(startIndex.plus(level.inc()), endIndex)

                    val element = Element.Header(level, text)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                //QUOTES
                3 -> {
                    //text without "> "
                    text = string.subSequence(startIndex.plus(2), endIndex)
                    val subElements = findElements(text)
                    val element = Element.Quote(text, subElements)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                //ITALIC
                4 -> {
                    //text without "*{}*"
                    text = string.subSequence(startIndex.inc(), endIndex.dec())
                    val subElements = findElements(text)
                    val element = Element.Italic(text, subElements)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                //BOLD
                5 -> {
                    //text without "**{}**"
                    text = string.subSequence(startIndex.plus(2), endIndex.minus(2))
                    val subElements = findElements(text)
                    val element = Element.Bold(text, subElements)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                //STRIKE
                6 -> {
                    //text without "~~{}~~"
                    text = string.subSequence(startIndex.plus(2), endIndex.minus(2))
                    val subElements = findElements(text)
                    val element = Element.Strike(text, subElements)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                //RULE
                7 -> {
                    //text without "***" insert empty character
                    //TODO почему не очищаем текст тут?
                    val element = Element.Rule()
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                //INLINE CODE
                8 -> {
                    //text without "`{}`"
                    text = string.subSequence(startIndex.inc(), endIndex.dec())
                    val element = Element.InlineCode(text)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                //LINK
                9 -> {
                    //full text for regex
                    text = string.subSequence(startIndex, endIndex)
                    val (title: String, link: String) = "\\[(.*)]\\((.*)\\)".toRegex().find(text)!!.destructured
                    val element = Element.Link(link, title)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                //ORDERED LIST
                10 -> {
                    //full text for regex
                    text = string.substring(startIndex, endIndex)
                    //text without "\d+. "
                    val (order: String, clearedText: String)= "^(\\d+\\.) (.+)\$".toRegex().find(text)!!.destructured

                    //find inner elements
                    val subs = findElements(clearedText)
                    val element = Element.OrderedListItem(order, clearedText, subs)
                    parents.add(element)

                    //next find start from position "endIndex" (last regex character)
                    lastStartIndex = endIndex
                }

                //BLOCK CODE
                11 -> {
                    //text without "```{}```"
                    text = string.subSequence(startIndex.plus(3), endIndex.minus(3))
                    if (!text.contains(LINE_SEPARATOR)) {
                        val element = Element.BlockCode(Element.BlockCode.Type.SINGLE, text)
                        parents.add(element)
                    } else {
                        val strings = text.split(LINE_SEPARATOR.toRegex())
                        val firstLine = Element.BlockCode(Element.BlockCode.Type.START, strings.first().plus(
                            LINE_SEPARATOR))
                        parents.add(firstLine)
                        if (strings.size > 2) {
                            (1..strings.size.minus(2)).forEach {
                                val element = Element.BlockCode(Element.BlockCode.Type.MIDDLE, strings[it].plus(
                                    LINE_SEPARATOR))
                                parents.add(element)
                            }
                        }
                        val lastLine = Element.BlockCode(Element.BlockCode.Type.END, strings.last())
                        parents.add(lastLine)
                    }
                    lastStartIndex = endIndex
                }
            }
        }

        if (lastStartIndex < string.length) {
            val text = string.subSequence(lastStartIndex, string.length)
            parents.add(Element.Text(text))
        }

        return parents
    }
}
data class MarkdownText(val elements: List<Element>)

sealed class Element() {
    abstract val text: CharSequence
    abstract val elements: List<Element>

    data class Text(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class UnorderedListItem(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Header(
        val level: Int = 1,
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Quote(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Italic(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Bold(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Strike(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Rule(
        override val text: CharSequence = " ",
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class InlineCode(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Link(
        val link: String,
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class OrderedListItem(
        val order: String,
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class BlockCode(
        val type: Type = Type.MIDDLE,
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element() {
        enum class Type { START, END, MIDDLE, SINGLE }
    }

}