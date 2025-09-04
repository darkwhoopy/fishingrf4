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

    // Version simplifiée qui utilise uniquement les ressources générées
    private fun resolveWebClientId(): String {
        return try {
            val id = activity.getString(R.string.default_web_client_id)
            Log.d("AuthManager", "Web Client ID: ${id.take(20)}...")
            id
        } catch (e: Exception) {
            Log.e("AuthManager", "Erreur récupération Web Client ID", e)
            throw RuntimeException("Web Client ID non trouvé. Vérifiez google-services.json")
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
                .addOnSuccessListener {
                    Log.d("AuthManager", "Connexion réussie")
                    onSuccess()
                }
                .addOnFailureListener {
                    Log.e("AuthManager", "Erreur Firebase", it)
                    onError(it.message ?: "Erreur de connexion")
                }
        } catch (e: ApiException) {
            Log.e("AuthManager", "Erreur Google Sign-In: ${e.statusCode}")
            onError("Échec Google Sign-In: ${e.statusCode}")
        }
    }

    fun signOut() { auth.signOut() }

    fun signOutGoogle(onDone: () -> Unit = {}) {
        googleClient().signOut()
            .addOnCompleteListener { onDone() }
    }

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
}