package ru.skillbranch.skillarticles.extensions


fun String?.indexesOf(needle: String, ignoreCase: Boolean = true): List<Int> {

    val indexes = mutableListOf<Int>()

    if (this.isNullOrEmpty() || needle.isEmpty()) return indexes

    var currentIdx = this.indexOf(needle, 0, ignoreCase)

    while (currentIdx != -1) {
        indexes.add(currentIdx)
        currentIdx = this.indexOf(needle, currentIdx + 1, ignoreCase)
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