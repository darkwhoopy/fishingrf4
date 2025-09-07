package com.rf4.fishingrf4.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rf4.fishingrf4.R

class AuthManager(private val activity: Activity) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        private const val TAG = "AuthManager"
    }

    // Version simplifiée qui utilise uniquement les ressources générées
    private fun resolveWebClientId(): String {
        return try {
            val id = activity.getString(R.string.default_web_client_id)
            Log.d(TAG, "Web Client ID résolu: $id")
            id
        } catch (e: Exception) {
            Log.e(TAG, "ERREUR lors de la résolution du Web Client ID", e)
            // Fallback avec votre Web Client ID fonctionnel
            val fallbackId = "908932428291-75sbfttt018n6dc9lq40mboak5kg3tep.apps.googleusercontent.com"
            Log.w(TAG, "Utilisation du fallback Web Client ID: $fallbackId")
            fallbackId
        }
    }

    fun googleClient(): GoogleSignInClient {
        Log.d(TAG, "Création du GoogleSignInClient")
        val webClientId = resolveWebClientId()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(activity, gso)
        Log.d(TAG, "GoogleSignInClient créé avec succès")
        return client
    }

    fun handleSignInResult(
        data: Intent?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "🔥 Début handleSignInResult")
        Log.d(TAG, "Intent data présent: ${data != null}")

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d(TAG, "✅ Account obtenu: ${account?.email}")
            Log.d(TAG, "ID Token présent: ${account?.idToken != null}")
            Log.d(TAG, "Display Name: ${account?.displayName}")

            if (account?.idToken == null) {
                Log.e(TAG, "❌ ID Token est null!")
                onError("Token d'authentification manquant")
                return
            }

            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            Log.d(TAG, "🔑 Credential créé, tentative d'authentification Firebase...")

            // Vérification de l'état actuel avant auth
            val currentUserBefore = auth.currentUser
            Log.d(TAG, "Utilisateur avant auth: ${currentUserBefore?.email ?: "AUCUN"}")

            auth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    Log.d(TAG, "🎉 Firebase auth SUCCESS!")
                    Log.d(TAG, "Utilisateur connecté: ${result.user?.email}")
                    Log.d(TAG, "UID: ${result.user?.uid}")
                    Log.d(TAG, "Appel de onSuccess()...")
                    onSuccess()
                }
                .addOnFailureListener { error ->
                    Log.e(TAG, "💥 Firebase auth FAILED: ${error.message}")
                    Log.e(TAG, "Error code: ${error.javaClass.simpleName}")
                    onError("Erreur Firebase: ${error.message}")
                }
        } catch (e: ApiException) {
            Log.e(TAG, "💥 Google Sign-In FAILED!")
            Log.e(TAG, "Status Code: ${e.statusCode}")
            Log.e(TAG, "Status Message: ${e.status}")
            Log.e(TAG, "Error Details: ${e.message}")

            val errorMsg = when (e.statusCode) {
                10 -> "Erreur de configuration développeur (DEVELOPER_ERROR)"
                12500 -> "Connexion annulée par l'utilisateur (SIGN_IN_CANCELLED)"
                7 -> "Erreur réseau (NETWORK_ERROR)"
                else -> "Erreur Google Sign-In (${e.statusCode})"
            }

            onError(errorMsg)
        }
    }

    fun signOut() {
        Log.d(TAG, "🚪 Déconnexion Firebase")
        val userBefore = auth.currentUser?.email
        auth.signOut()
        Log.d(TAG, "Utilisateur déconnecté: $userBefore")
    }

    fun signOutGoogle(onDone: () -> Unit = {}) {
        Log.d(TAG, "🚪 Déconnexion Google Sign-In")
        googleClient().signOut()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "✅ Déconnexion Google réussie")
                } else {
                    Log.w(TAG, "⚠️ Problème lors de la déconnexion Google", task.exception)
                }
                onDone()
            }
    }

    fun revokeAccess(
        onDone: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        Log.d(TAG, "🗑️ Révocation d'accès Google")
        googleClient().revokeAccess()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "✅ Révocation d'accès réussie")
                    onDone()
                } else {
                    val errorMsg = task.exception?.message ?: "Erreur inconnue"
                    Log.e(TAG, "💥 Erreur lors de la révocation: $errorMsg", task.exception)
                    onError(errorMsg)
                }
            }
    }

    fun currentUser() = auth.currentUser

    fun isUserSignedIn(): Boolean {
        val user = auth.currentUser
        val isSignedIn = user != null
        Log.d(TAG, "Vérification connexion: ${if (isSignedIn) "CONNECTÉ (${user?.email})" else "NON CONNECTÉ"}")
        return isSignedIn
    }

    // Méthode utilitaire pour déboguer l'état auth
    fun debugAuthState() {
        Log.d(TAG, "=== DEBUG AUTH STATE ===")
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d(TAG, "✅ Utilisateur connecté:")
            Log.d(TAG, "  - Email: ${currentUser.email}")
            Log.d(TAG, "  - UID: ${currentUser.uid}")
            Log.d(TAG, "  - Display Name: ${currentUser.displayName}")
            Log.d(TAG, "  - Email vérifié: ${currentUser.isEmailVerified}")
        } else {
            Log.d(TAG, "❌ Aucun utilisateur connecté")
        }
        Log.d(TAG, "========================")
    }
}