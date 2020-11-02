package ru.skillbranch.skillarticles.data.remote.req

data class MessageReq(
    val message: String,
    val answerTo: String? = null
)

data class RefreshReq(
    val refreshToken: String,
    val accessToken: String
)