package com.rf4.fishingrf4.auth

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rf4.fishingrf4.R

class AuthManager(private val activity: Activity) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Fallback si la ressource générée n’existe pas
    private fun resolveWebClientId(): String {
        return try {
            val id = activity.getString(R.string.default_web_client_id)
            if (id.isNullOrBlank() || id == "default_web_client_id") FALLBACK_WEB_CLIENT_ID else id
        } catch (_: Exception) {
            FALLBACK_WEB_CLIENT_ID
        }
    }

    fun googleClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resolveWebClientId())
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }

    fun handleSignInResult(
        data: Intent?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onError(it.message ?: "Erreur de connexion") }
        } catch (e: ApiException) {
            onError("Échec Google Sign-In: ${e.statusCode}")
        }
    }

    // Déconnexion Firebase uniquement (tu l'as déjà)
    fun signOut() { auth.signOut() }

    // Déconnexion côté GoogleSignIn (fait apparaître le sélecteur au prochain login)
    fun signOutGoogle(onDone: () -> Unit = {}) {
        googleClient().signOut()
            .addOnCompleteListener { onDone() }
    }

    // Révoquer l'autorisation (oublier le compte pour cette app)
    fun revokeAccess(
        onDone: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        googleClient().revokeAccess()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onDone()
                else onError(task.exception?.message ?: "Impossible de révoquer l'accès")
            }
    }

    fun currentUser() = auth.currentUser

    companion object {
        // 👇 Ton client OAuth Web depuis google-services.json
        private const val FALLBACK_WEB_CLIENT_ID =
            "908932428291-75sbfttt018n6dc9lq40mboak5kg3tep.apps.googleusercontent.com"
    }
}
