package ru.skillbranch.skillarticles.data.delegates

import androidx.core.content.edit
import ru.skillbranch.skillarticles.data.local.PrefManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefDelegate<T>(private val defaultValue: T) {
    private var storedValue: T? = null

    operator fun provideDelegate(
        thisRef: PrefManager,
        prop: KProperty<*>
    ): ReadWriteProperty<PrefManager, T?> {
        val key = prop.name

        return object : ReadWriteProperty<PrefManager, T?> {
            override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? {
                if (storedValue == null) {
                    @Suppress("UNCHECKED_CAST")
                    storedValue = when (defaultValue) {
                        is Int -> thisRef.preferences.getInt(property.name, defaultValue) as T
                        is Long -> thisRef.preferences.getLong(property.name, defaultValue) as T
                        is Float -> thisRef.preferences.getFloat(property.name, defaultValue) as T
                        is String -> thisRef.preferences.getString(property.name, defaultValue) as T
                        is Boolean -> thisRef.preferences.getBoolean(property.name, defaultValue) as T
                        else -> error("This type can not be stored into Preferences")
                    }
                }
                return storedValue
            }

            override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
                thisRef.preferences.edit {
                    when (value) {
                        is Int -> putInt(key, value)
                        is Long -> putLong(key, value)
                        is Float -> putFloat(key, value)
                        is String -> putString(key, value)
                        is Boolean -> putBoolean(key, value)
                        else -> error("Only primitive types can   be stored into Preferences")
                    }
                }
                storedValue = value
            }
        }
    }
}
