package ru.skillbranch.skillarticles.data.remote.interceptors

import dagger.Lazy
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.RestService
import ru.skillbranch.skillarticles.data.remote.req.RefreshReq
import ru.skillbranch.skillarticles.di.modules.NetworkModule
import java.io.IOException

class TokenAuthenticator(
    private val prefs: PrefManager,
    private val lazyApi: Lazy<RestService>
) : Authenticator {
    /**
     * Authenticator for when the authToken need to be refresh and updated
     * every time we get a 401 error code
     */
    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {

        if (response.code != 401) return null

        val refreshRes = lazyApi.get().refresh(RefreshReq(prefs.refreshToken)).execute()

        if (!refreshRes.isSuccessful) return null

        val tokens = refreshRes.body()!!
        val access = NetworkModule.getAccessTokenWithType(tokens.accessToken)
        prefs.accessToken = access
        prefs.refreshToken = tokens.refreshToken

        return response.request.newBuilder()
            .header("Authorization", access)
            .build()
    }

}
