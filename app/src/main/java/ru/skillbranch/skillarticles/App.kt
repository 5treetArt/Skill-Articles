package ru.skillbranch.skillarticles

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.NetworkMonitor
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var monitor: NetworkMonitor

    @Inject
    lateinit var preferences: PrefManager

    override fun onCreate() {
        super.onCreate()

        monitor.registerNetworkMonitor()

        val mode = if (preferences.isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO

        AppCompatDelegate.setDefaultNightMode(mode)
    }
}