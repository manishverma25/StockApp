package com.manish.stockapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat.startActivityForResult
//import com.firebase.ui.auth.AuthUI;

import android.content.Intent
import android.view.View
import com.firebase.ui.auth.AuthUI

import java.util.Arrays




class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


//    fun handleLoginRegister() {
//        val providers: List<AuthUI.IdpConfig> = Arrays.asList(
//            AuthUI.IdpConfig.EmailBuilder().build(),
//            AuthUI.IdpConfig.GoogleBuilder().build(),
//            AuthUI.IdpConfig.PhoneBuilder().build()
//        )
//        val intent: Intent = AuthUI.getInstance()
//            .createSignInIntentBuilder()
//            .setAvailableProviders(providers)
//            .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
//            .setLogo(R.drawable.notes)
//            .setAlwaysShowSignInMethodScreen(true)
//            .setIsSmartLockEnabled(false)
//            .build()
//        startActivityForResult(intent, Companion.AUTHUI_REQUEST_CODE)
//
//
//    }

    companion object {
        val  AUTHUI_REQUEST_CODE = 100
    }
}