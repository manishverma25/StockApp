package com.manish.stockapp.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import com.firebase.ui.auth.AuthUI;

import android.content.Intent
import android.util.Log
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.manish.stockapp.R

import java.util.Arrays




class LoginActivity : AppCompatActivity() {
    val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun handleLogin(view: View) {

        Log.d(TAG," handleLogin click ")
        val providers: List<AuthUI.IdpConfig> = Arrays.asList(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )
        val intent: Intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
//            .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
            .setLogo(R.drawable.notes)
            .setAlwaysShowSignInMethodScreen(true)
            .setIsSmartLockEnabled(false)
            .build()
        startActivityForResult(intent, AUTHUI_REQUEST_CODE)
    }

    companion object {
        val  AUTHUI_REQUEST_CODE = 100
    }
}