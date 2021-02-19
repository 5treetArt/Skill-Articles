package ru.skillbranch.skillarticles.data.repositories

import androidx.lifecycle.LiveData
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.RestService
import ru.skillbranch.skillarticles.data.remote.req.LoginReq
import ru.skillbranch.skillarticles.data.remote.req.RegisterReq
import ru.skillbranch.skillarticles.data.remote.res.AuthRes
import ru.skillbranch.skillarticles.di.modules.NetworkModule
import javax.inject.Inject

class RootRepository @Inject constructor(
    private val preferences: PrefManager,
    private val network: RestService
) : IRepository {
    fun isAuth(): LiveData<Boolean> = preferences.isAuthLive

    suspend fun login(login: String, password: String) {
        val auth = network.login(LoginReq(login, password))
        updatePreferences(auth)
    }


    suspend fun register(name: String, login: String, password: String) {
        val auth = network.register(RegisterReq(name, login, password))
        updatePreferences(auth)
    }

    private fun updatePreferences(auth: AuthRes) {
        preferences.profile = auth.user
        preferences.accessToken = NetworkModule.getAccessTokenWithType(auth.accessToken)
        preferences.refreshToken = auth.refreshToken
    }
}