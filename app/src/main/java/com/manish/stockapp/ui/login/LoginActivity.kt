package com.manish.stockapp.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import com.firebase.ui.auth.AuthUI;

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.manish.stockapp.R

import java.util.Arrays
import com.firebase.ui.auth.IdpResponse

import com.manish.stockapp.ui.base.MainActivity

import com.google.firebase.auth.FirebaseAuth



class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
    }


    fun handleLogin(view: View) {

        val providers: List<AuthUI.IdpConfig> = Arrays.asList(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )
        val intent: Intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setAlwaysShowSignInMethodScreen(true)
            .setIsSmartLockEnabled(false)
            .build()
        startActivityForResult(intent, AUTHUI_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTHUI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Signing in failed
                val response = IdpResponse.fromResultIntent(data)
                if (response == null) {
                    Log.d(Companion.TAG, "onActivityResult: the user has cancelled the sign in request")
                } else {
                    val errorMsg = response.error?.message?: getString(R.string.sign_in_error)
                    Toast.makeText(this,errorMsg,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        val  AUTHUI_REQUEST_CODE = 100
        val TAG = "LoginActivity"
    }
}