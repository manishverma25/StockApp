package com.manish.stockapp.ui.base

import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment()  {


     fun hideProgressBar(view : View) {
        view.visibility = View.GONE
    }

     fun showProgressBar(view : View) {
        view.visibility = View.VISIBLE
    }
}