package com.manish.stockapp.ui.splash

import android.os.Bundle
import android.util.Log
import com.manish.stockapp.R
import com.manish.stockapp.ui.base.BaseActivity


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