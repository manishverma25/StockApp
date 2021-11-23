package com.manish.stockapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.util.Log
import android.view.View


class SplashActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {
    val TAG = "SplashActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

    }


    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        Log.d(TAG," firebaseAuth.currentUse $firebaseAuth.currentUse ")
        if (firebaseAuth.currentUser == null) {
            startLoginActivity()
            return
        }
        navigateToHome();

    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun navigateToHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun handleLoginRegister(view: View) {}

}