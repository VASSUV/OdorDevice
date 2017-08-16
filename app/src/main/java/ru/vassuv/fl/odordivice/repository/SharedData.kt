package ru.vassuv.fl.odordivice.repository

import android.content.SharedPreferences
import android.preference.PreferenceManager
import ru.vassuv.fl.odordivice.App.Companion.context

enum class SharedData {
    USER_ID, USER_NAME, USER_FULL_NAME,
    TOKEN;

    private val instance: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    fun getString() = instance.getString(name, "")
    fun getInt() = instance.getInt(name, 0)
    fun getBoolean() = instance.getBoolean(name, false)
    fun getLong() = instance.getLong(name, 0)

    fun saveString(value: String) = instance.edit().putString(name, value).apply()
    fun saveInt(value: Int) = instance.edit().putInt(name, value).apply()
    fun saveBoolean(value: Boolean) = instance.edit().putBoolean(name, value).apply()
    fun saveLong(value: Long) = instance.edit().putLong(name, value).apply()

    fun remove() = instance.edit().remove(name).apply()

}
