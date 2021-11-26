package com.manish.stockapp.ui.login

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import com.firebase.ui.auth.AuthUI;

import android.content.Intent
import android.util.Log
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.manish.stockapp.R

import java.util.Arrays
import com.firebase.ui.auth.IdpResponse

import com.manish.stockapp.ui.base.MainActivity

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*


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
//            .setLogo(R.drawable.)
            .setAlwaysShowSignInMethodScreen(true)
            .setIsSmartLockEnabled(false)
            .build()
        startActivityForResult(intent, AUTHUI_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTHUI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // We have signed in the user or we have a new user
                val user = FirebaseAuth.getInstance().currentUser
                Log.d(TAG, "onActivityResult: " + user.toString())
                //Checking for User (New/Old)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Signing in failed
                val response = IdpResponse.fromResultIntent(data)
                if (response == null) {
                    Log.d(TAG, "onActivityResult: the user has cancelled the sign in request")
                } else {
                    Log.e(TAG, "onActivityResult: ", response.error)
                }
            }
        }
    }

    companion object {
        val  AUTHUI_REQUEST_CODE = 100
    }
}