package com.rf4.fishingrf4.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.*

object LanguageManager {

    private const val PREF_LANGUAGE = "app_language"

    enum class Language(val code: String, val displayName: String) {
        FRENCH("fr", "Français"),
        ENGLISH("en", "English")
    }

    /**
     * Obtient la langue actuellement configurée
     */
    fun getCurrentLanguage(context: Context): Language {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val savedCode = prefs.getString(PREF_LANGUAGE, "fr")
        return Language.values().find { it.code == savedCode } ?: Language.FRENCH
    }

    /**
     * Obtient la langue sauvegardée (alias pour compatibilité)
     */
    fun getSavedLanguage(context: Context): Language {
        return getCurrentLanguage(context)
    }

    /**
     * ✅ MÉTHODE MANQUANTE : Définit la langue de l'application
     */
    fun setAppLanguage(context: Context, language: Language) {
        val locale = Locale(language.code)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        // Sauvegarder le choix
        saveLanguage(context, language)
    }

    /**
     * Sauvegarde la préférence de langue
     */
    private fun saveLanguage(context: Context, language: Language) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_LANGUAGE, language.code).apply()
    }

    /**
     * Applique la langue sauvegardée au démarrage de l'application
     */
    fun applyLanguage(context: Context) {
        val savedLanguage = getCurrentLanguage(context)
        setAppLanguage(context, savedLanguage)
    }
}