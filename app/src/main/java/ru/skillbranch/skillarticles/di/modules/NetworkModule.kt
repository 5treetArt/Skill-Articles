package ru.skillbranch.skillarticles.di.modules

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.skillbranch.skillarticles.AppConfig
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.NetworkMonitor
import ru.skillbranch.skillarticles.data.remote.RestService
import ru.skillbranch.skillarticles.data.remote.adapters.DateAdapter
import ru.skillbranch.skillarticles.data.remote.interceptors.ErrorStatusInterceptor
import ru.skillbranch.skillarticles.data.remote.interceptors.NetworkStatusInterceptor
import ru.skillbranch.skillarticles.data.remote.interceptors.TokenAuthenticator
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideRestService(retrofit: Retrofit): RestService =
        retrofit.create(RestService::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .client(client) // set http client
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // set json converter/parser
        .baseUrl(AppConfig.BASE_URL)
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        authenticator: TokenAuthenticator,
        statusInterceptor: NetworkStatusInterceptor,
        errInterceptor: ErrorStatusInterceptor
    ): OkHttpClient = OkHttpClient().newBuilder()
        .readTimeout(2, TimeUnit.SECONDS)  // socket timeout (GET)
        .writeTimeout(5, TimeUnit.SECONDS) // socket timeout (POST, PUT, etc)
        .authenticator(authenticator)
        .addInterceptor(statusInterceptor)
        .addInterceptor(logging)             // intercept req/res for logging
        .addInterceptor(errInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        prefs: PrefManager,
        lazyApi: Lazy<RestService>
    ): TokenAuthenticator = TokenAuthenticator(prefs, lazyApi)

    @Provides
    @Singleton
    fun provideNetworkStatusInterceptor(monitor: NetworkMonitor): NetworkStatusInterceptor =
        NetworkStatusInterceptor(monitor)

    @Provides
    @Singleton
    fun provideErrorStatusInterceptor(moshi: Moshi): ErrorStatusInterceptor =
        ErrorStatusInterceptor(moshi)

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        // convert long timestamp to date
        .add(DateAdapter())
        // convert json to class by reflection
        .add(KotlinJsonAdapterFactory()) // lecture 11, time code 01:58:07
        .build()

    fun getAccessTokenWithType(accessToken: String) = "Bearer $accessToken"
}