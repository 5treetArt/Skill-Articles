package ru.skillbranch.skillarticles.extensions

import ru.skillbranch.skillarticles.utils.BoyerMoore


fun String?.indexesOf(needle: String): List<Int> {

    val indexes = mutableListOf<Int>()

    if (this.isNullOrEmpty() || needle.isEmpty()) return indexes

    var currentIdx = this.indexOf(needle, 0, true)

    while (currentIdx != -1) {
        indexes.add(currentIdx)
        currentIdx = this.indexOf(needle, currentIdx + 1, true)
    }

    return indexes

    //TODO boyer moore
    //val indexes = mutableListOf<Int>()
    //if (this == null || needle.isEmpty()) return indexes
//
    //val needleArr = needle.toCharArray()
    //var currentIdx = BoyerMoore.indexOf(this.toCharArray(), needleArr)
//
    //while (currentIdx != -1) {
    //    indexes.add(currentIdx)
    //    currentIdx = BoyerMoore.indexOf(this.substring(currentIdx + needleArr.size).toCharArray(), needleArr)
    //}
//
    //return indexes
}