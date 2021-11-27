package com.manish.stockapp.ui.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.manish.stockapp.R
import com.manish.stockapp.StockApplication
import com.manish.stockapp.ui.home.HomeFragment
import com.manish.stockapp.ui.login.LoginActivity
import com.manish.stockapp.ui.wishlist.WishListFragment
import com.manish.stockapp.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("mvv", " onCreate 222 ")
        setContentView(R.layout.main_activity)
        injectDI()
        setupBottomBar()

    }

    private fun injectDI() {
        StockApplication.appComponent.inject(this)
    }

    fun setupBottomBar() {
        bottomBar.setOnItemSelectedListener {
            when (it) {
                0 -> openFragment(HomeFragment.newInstance())
                1 -> openFragment(WishListFragment.newInstance())
                2 -> openFragment(ProfileFragment.newInstance())
            }
        }
        if (FirebaseAuth.getInstance().currentUser == null) {
            startLoginActivity()
            return
        }
        openFragment(HomeFragment.newInstance())

    }

    fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        Log.d(Companion.TAG, " BaseActivity  firebaseAuth.currentUse ${firebaseAuth.currentUser} ")
        if (firebaseAuth.currentUser == null) {
            startLoginActivity()
            return
        }

    }

    override fun onStart() {
        super.onStart()

        Log.d(
            TAG,
            " FirebaseAuth.getInstance().currentUser :::  ${FirebaseAuth.getInstance().currentUser} "
        )
        if (FirebaseAuth.getInstance().currentUser == null) {
            startLoginActivity()
        }
    }


    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    companion object {
        val TAG = "MainActivity"
    }


}