package com.rf4.fishingrf4

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.rf4.fishingrf4.ui.FishingRF4App
import com.rf4.fishingrf4.utils.LanguageManager
import com.rf4.fishingrf4.data.utils.GameTimeManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Appliquer la langue sauvegardée avant super.onCreate()
        LanguageManager.applyLanguage(this)
        super.onCreate(savedInstanceState)

        // ✅ NOUVEAU : Démarrer le GameTimeManager
        lifecycleScope.launch {
            GameTimeManager.start()
        }
        setContent { MaterialTheme { FishingRF4App() }
            ScreenLockManager()
        }
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        newBase?.let {
            LanguageManager.applyLanguage(it)
        }
    }
}
@Composable
fun ScreenLockManager() {
    val context = LocalContext.current
    val activity = context as? Activity

    // Lire les préférences (à adapter selon votre système)
    var keepScreenOn by remember { mutableStateOf(true) } // Par défaut activé

    LaunchedEffect(keepScreenOn) {
        activity?.let {
            if (keepScreenOn) {
                it.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                it.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
}
