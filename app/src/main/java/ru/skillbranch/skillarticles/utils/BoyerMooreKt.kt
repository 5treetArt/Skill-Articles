package ru.skillbranch.skillarticles.utils

fun indexOf(haystack: String, needle: String): Int =
    BoyerMoore.indexOf(haystack.toCharArray(), needle.toCharArray())
