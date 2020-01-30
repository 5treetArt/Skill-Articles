package ru.skillbranch.skillarticles.data.delegates

import androidx.core.content.edit
import ru.skillbranch.skillarticles.data.local.PrefManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefDelegate<T>(private val defaultValue: T) : ReadWriteProperty<PrefManager, T?> {
    override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? {
        return when (defaultValue) {
            is Boolean -> thisRef.preferences.getBoolean(property.name, defaultValue) as T
            is String -> thisRef.preferences.getString(property.name, defaultValue) as T
            is Float -> thisRef.preferences.getFloat(property.name, defaultValue) as T
            is Int -> thisRef.preferences.getInt(property.name, defaultValue) as T
            is Long -> thisRef.preferences.getLong(property.name, defaultValue) as T
            else -> null
        }
    }

    override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
        thisRef.preferences.edit(true) {
            when (defaultValue) {
                is Boolean -> putBoolean(property.name, defaultValue)
                is String -> putString(property.name, defaultValue)
                is Float -> putFloat(property.name, defaultValue)
                is Int -> putInt(property.name, defaultValue)
                is Long -> putLong(property.name, defaultValue)
            }
        }
    }

}