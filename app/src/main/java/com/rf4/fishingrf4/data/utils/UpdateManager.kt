package com.rf4.fishingrf4.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Gestionnaire des annonces de mise à jour
 * Contrôle l'affichage unique du popup de mise à jour
 */
class UpdateManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "fishing_rf4_updates",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_LAST_SHOWN_VERSION = "last_shown_update_version"
        const val CURRENT_VERSION = "v0.7" // Mettre à jour à chaque release
    }

    /**
     * Vérifie si le popup de mise à jour doit être affiché
     * @return true si le popup doit être montré, false sinon
     */
    fun shouldShowUpdatePopup(): Boolean {
        val lastShownVersion = prefs.getString(KEY_LAST_SHOWN_VERSION, "")
        return lastShownVersion != CURRENT_VERSION
    }

    /**
     * Marque la version actuelle comme vue par l'utilisateur
     */
    fun markUpdatePopupShown() {
        prefs.edit()
            .putString(KEY_LAST_SHOWN_VERSION, CURRENT_VERSION)
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
     * Obtient la version actuelle
     */
    fun getCurrentVersion(): String = CURRENT_VERSION
}