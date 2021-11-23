package com.manish.stockapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.util.Log
import android.widget.Toast

import android.view.Menu
import com.firebase.ui.auth.AuthUI

import android.view.MenuItem








class SplashActivity : BaseActivity()/*, FirebaseAuth.AuthStateListener */{
    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(TAG," SplashActivity  ....")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        navigateToNext()
//        startLoginActivity()

    }


//    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
//        Log.d(Companion.TAG," firebaseAuth.currentUse $firebaseAuth.currentUse ")
//        if (firebaseAuth.currentUser == null) {
//            startLoginActivity()
//            return
//        }
//        navigateToHome();
//
//    }

//    private fun startLoginActivity() {
//        val intent = Intent(this, LoginActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//
//    private fun navigateToHome(){
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
//    }

//    fun navigateToNext() {
//
//        Log.d(Companion.TAG," FirebaseAuth.getInstance().currentUser :::  ${FirebaseAuth.getInstance().currentUser} ")
//
//        if (FirebaseAuth.getInstance().currentUser == null) {
//            startLoginActivity()
//        } else {
//            navigateToHome()
//            Toast.makeText(this, "Please Login First", Toast.LENGTH_LONG).show();
//        }
//    }

    companion object {
        val TAG = "SplashActivity"
    }


}