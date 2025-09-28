package com.rf4.fishingrf4.utils

import android.content.Context
import android.content.SharedPreferences
import com.rf4.fishingrf4.BuildConfig
import com.rf4.fishingrf4.ui.components.PopupMode

/**
 * Gestionnaire intelligent des popups d'accueil et de mise à jour
 * Contrôle l'affichage unique selon le contexte utilisateur
 */
class UpdateManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "fishing_rf4_updates",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_LAST_SHOWN_VERSION = "last_shown_update_version"
        private const val KEY_FIRST_LAUNCH = "is_first_launch"

        /**
         * Version actuelle récupérée automatiquement depuis build.gradle
         */
        val CURRENT_VERSION: String
            get() = "v${BuildConfig.VERSION_NAME}"
    }

    /**
     * Détermine quel type de popup afficher
     * @return PopupMode.WELCOME pour nouveaux utilisateurs,
     *         PopupMode.UPDATE pour mise à jour,
     *         null si aucun popup nécessaire
     */
    fun getPopupMode(): PopupMode? {
        // Premier lancement = popup d'accueil
        if (isFirstLaunch()) {
            return PopupMode.WELCOME
        }

        // Mise à jour disponible pour utilisateur existant
        if (shouldShowUpdatePopup()) {
            return PopupMode.UPDATE
        }

        return null // Pas de popup nécessaire
    }

    /**
     * Vérifie si c'est le premier lancement de l'app
     */
    private fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    /**
     * Vérifie si le popup de mise à jour doit être affiché
     * (uniquement pour utilisateurs existants)
     */
    private fun shouldShowUpdatePopup(): Boolean {
        val lastShownVersion = prefs.getString(KEY_LAST_SHOWN_VERSION, "")
        return lastShownVersion != CURRENT_VERSION && !isFirstLaunch()
    }

    /**
     * Marque le popup comme vu par l'utilisateur
     * (fonctionne pour WELCOME et UPDATE)
     */
    fun markPopupShown() {
        prefs.edit()
            .putString(KEY_LAST_SHOWN_VERSION, CURRENT_VERSION)
            .putBoolean(KEY_FIRST_LAUNCH, false) // Plus jamais premier lancement
            .apply()
    }

    /**
     * Force l'affichage du popup (pour debug ou admin)
     */
    fun resetUpdatePopup() {
        prefs.edit()
            .remove(KEY_LAST_SHOWN_VERSION)
            .apply()
    }

    /**
     * Reset complet - marque comme premier lancement (pour debug)
     */
    fun resetToFirstLaunch() {
        prefs.edit()
            .clear()
            .apply()
    }

    /**
     * Obtient la version actuelle
     */
    fun getCurrentVersion(): String = CURRENT_VERSION

    /**
     * Vérifie si l'utilisateur est nouveau (pour analytics)
     */
    fun isNewUser(): Boolean = isFirstLaunch()
}