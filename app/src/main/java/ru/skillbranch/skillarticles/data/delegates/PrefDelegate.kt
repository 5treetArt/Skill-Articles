package ru.skillbranch.skillarticles.data.delegates

import androidx.core.content.edit
import ru.skillbranch.skillarticles.data.local.PrefManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefDelegate<T>(private val defaultValue: T) : ReadWriteProperty<PrefManager, T?> {
    override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? {
        return when (defaultValue) {
            is String -> thisRef.preferences.getString(property.name, defaultValue) as T
            is Int -> thisRef.preferences.getInt(property.name, defaultValue) as T
            is Boolean -> thisRef.preferences.getBoolean(property.name, defaultValue) as T
            is Long -> thisRef.preferences.getLong(property.name, defaultValue) as T
            is Float -> thisRef.preferences.getFloat(property.name, defaultValue) as T
            else -> null
        }
    }

    override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
        thisRef.preferences.edit(true) {
            when (defaultValue) {
                is String -> putString(property.name, value as String)
                is Int -> putInt(property.name, value as Int)
                is Boolean -> putBoolean(property.name, value as Boolean)
                is Float -> putFloat(property.name, value as Float)
                is Long -> putLong(property.name, value as Long)
            }
        }
    }

}