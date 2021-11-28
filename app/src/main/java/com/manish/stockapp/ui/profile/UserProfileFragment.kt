package com.manish.stockapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.manish.stockapp.R
import com.manish.stockapp.StockApplication
import com.manish.stockapp.ViewModelFactory
import com.manish.stockapp.data.Resource
import com.manish.stockapp.ui.login.LoginActivity
import com.manish.stockapp.util.extension.errorSnack
import kotlinx.android.synthetic.main.profile_fragment.*
import javax.inject.Inject


class UserProfileFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDI()
        initUI()
        observerLiveData()
    }


    private fun injectDI() {
        StockApplication.appComponent.inject(this)
    }

    private fun initUI() {
        signOutTxt.setOnClickListener {
//            AuthUI.getInstance().signOut(ProfileFragment@this.requireContext())
//            startLoginActivity()
            profileViewModel.userLoggingOut()
        }

    }

    private fun observerLiveData() {
        observeruserNameLiveData()
        observerUserLogoutLiveData()
    }
    private fun observeruserNameLiveData() {
        profileViewModel.userNameLiveData.observe(viewLifecycleOwner, Observer {  response->
            handleUserNameResposne(response)
        })
    }

    private fun observerUserLogoutLiveData() {
        profileViewModel.signOutSuccessStatusLivaData.observe(viewLifecycleOwner, Observer {  response->
            handleUserLogoutResposne(response)
        })
    }

    private fun handleUserNameResposne(response :Resource<String>){
        when (response) {
            is Resource.Success -> {
                userNameTxt.text = response.data
            }
            is Resource.Error -> {
                response.message?.let { message ->
                    Log.d(TAG, " message $message")
                    userNameTxt.errorSnack(message, Snackbar.LENGTH_LONG)
                }
            }
            is Resource.Loading -> {
            }
        }
    }

    private fun handleUserLogoutResposne(response :Resource<String>){
        when (response) {
            is Resource.Success -> {
                startLoginActivity()
            }
            is Resource.Error -> {
                response.message?.let { message ->
                    Log.d(TAG, " message $message")
                    userNameTxt.errorSnack(message, Snackbar.LENGTH_LONG)
                }
            }
            is Resource.Loading -> {
            }
        }
    }


    private fun showProgressBar() {
        progress.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progress.visibility = View.GONE
    }

    private fun startLoginActivity() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    companion object {
        fun newInstance( ): UserProfileFragment {
            return UserProfileFragment()
        }

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        val TAG = "ProfileFragment"
    }

}
