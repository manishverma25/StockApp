package com.manish.stockapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.manish.stockapp.R
import com.manish.stockapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.profile_fragment.*


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {

        userNameTxt.text = FirebaseAuth.getInstance().currentUser?.displayName

        signOutTxt.setOnClickListener {
            AuthUI.getInstance().signOut(ProfileFragment@this.requireContext())
            startLoginActivity()
        }

    }

    private fun startLoginActivity() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    companion object {
        fun newInstance( ): ProfileFragment {
            return ProfileFragment()
        }

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        val TAG = "ProfileFragment"
    }

}
