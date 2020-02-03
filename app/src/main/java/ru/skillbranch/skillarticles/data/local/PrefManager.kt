package ru.skillbranch.skillarticles.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class PrefManager(context: Context) {
    val preferences: SharedPreferences by lazy { PreferenceManager(context).sharedPreferences }

    fun clearAll() = preferences.edit { clear() }
}