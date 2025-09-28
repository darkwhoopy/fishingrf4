package com.rf4.fishingrf4.data.models

import androidx.annotation.StringRes
import com.rf4.fishingrf4.R

/**
 * Options de réinitialisation disponibles dans les paramètres
 */
enum class ResetOption(@StringRes val stringResId: Int) {
    ENTRIES_STATS(R.string.reset_option_entries_stats),
    LAKES(R.string.reset_option_lakes),
    BAITS(R.string.reset_option_baits),
    FAVORITES(R.string.reset_option_favorites)
}