package com.manish.stockapp.ui.activity

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.manish.stockapp.R

open class BaseActivity:  AppCompatActivity() , FirebaseAuth.AuthStateListener {
    val TAG = "BaseActivity"



    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        Log.d(TAG," BaseActivity  firebaseAuth.currentUse ${firebaseAuth.currentUser } ")
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

    fun navigateToNext() {
        Log.d(TAG," FirebaseAuth.getInstance().currentUser :::  ${FirebaseAuth.getInstance().currentUser} ")

        if (FirebaseAuth.getInstance().currentUser == null) {
            startLoginActivity()
        } else {
            navigateToHome()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        when (id) {
            R.id.action_logout -> {
                AuthUI.getInstance().signOut(this)
                //todo after log out, souyld navigate to  login
                return true
            }
            R.id.action_profile -> {
//                startActivity(Intent(this, ProfileActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}