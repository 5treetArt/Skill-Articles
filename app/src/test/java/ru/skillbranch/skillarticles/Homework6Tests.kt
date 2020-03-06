package ru.skillbranch.skillarticles

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.skillbranch.skillarticles.data.repositories.Element
import ru.skillbranch.skillarticles.data.repositories.MarkdownParser
import ru.skillbranch.skillarticles.extensions.groupByBounds

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class Homework6Tests {
    @Test
    fun group_by_bounds_test() {

        val input = listOf(
            Pair(2, 5),
            Pair(8, 20),
            Pair(22, 30),
            Pair(45, 50),
            Pair(70, 100)
        )

        val bounds = listOf(
            Pair(0, 10),
            Pair(10, 30),
            Pair(30, 50),
            Pair(50, 60),
            Pair(60, 100)
        )

        val expected = listOf(
            listOf(Pair(2, 5), Pair(8, 10)),
            listOf(Pair(10, 20), Pair(22, 30)),
            listOf(Pair(45, 50)),
            emptyList(),
            listOf(Pair(70, 100))
        )

        val actual = input.groupByBounds(bounds)

        assertEquals(expected, actual)
    }
}