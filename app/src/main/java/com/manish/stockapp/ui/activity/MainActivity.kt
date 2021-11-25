package com.manish.stockapp.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.manish.stockapp.R
import com.manish.stockapp.ui.fragment.DashboardFragment
import com.manish.stockapp.ui.fragment.WishListFragment
import com.manish.stockapp.ui.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

import android.view.Menu




class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
    }


    fun setupUI(){
        Log.d("mvv", " setUI" )
        //todo  mvg set default first item selected
        bottomBar.setOnItemSelectedListener {
            Log.d("mvv", " pos  setOnItemSelectedListener " + it)
            when (it) {
                0 -> openFragment(DashboardFragment. newInstance("", ""))
                1 -> openFragment(WishListFragment. newInstance("", ""))
                2 -> openFragment(ProfileFragment. newInstance("", ""))
            }
        }

        openFragment(DashboardFragment. newInstance("", ""))

//        bottomBar.itemActiveIndex = 0


    }

    fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)

        transaction.commit()
    }


}