package com.rf4.fishingrf4.data.utils

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.*

object LanguageManager {

    private const val PREF_LANGUAGE = "app_language"

    enum class Language(val code: String, val displayName: String) {
        FRENCH("fr", "Français"),
        ENGLISH("en", "English")
    }

    fun saveLanguage(context: Context, language: Language) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_LANGUAGE, language.code).apply()
    }

    fun getSavedLanguage(context: Context): Language {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val savedCode = prefs.getString(PREF_LANGUAGE, "fr")
        return Language.values().find { it.code == savedCode } ?: Language.FRENCH
    }

    fun setAppLanguage(context: Context, language: Language) {
        val locale = Locale(language.code)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        saveLanguage(context, language)
    }
}