package com.manish.stockapp.ui.base

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.manish.stockapp.R
import com.manish.stockapp.StockApplication
import com.manish.stockapp.ui.home.HomeFragment
import com.manish.stockapp.ui.wishlist.WishListFragment
import com.manish.stockapp.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDI()
        setupUI()
    }

    private fun injectDI() {

        StockApplication.appComponent.inject(this)
    }


    fun setupUI(){
        Log.d("mvv", " setUI" )
        //todo  mvg set default first item selected
        bottomBar.setOnItemSelectedListener {
            Log.d("mvv", " pos  setOnItemSelectedListener " + it)
            when (it) {
                0 -> openFragment(HomeFragment. newInstance("", ""))
                1 -> openFragment(WishListFragment. newInstance("", ""))
                2 -> openFragment(ProfileFragment. newInstance("", ""))
            }
        }

        openFragment(HomeFragment. newInstance("", ""))

//        bottomBar.itemActiveIndex = 0


    }

    fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)

        transaction.commit()
    }


}