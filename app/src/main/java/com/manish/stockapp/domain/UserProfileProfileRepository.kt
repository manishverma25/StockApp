package com.manish.stockapp.domain

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.manish.stockapp.data.Resource
import javax.inject.Inject

class UserProfileProfileRepository @Inject constructor(val context: Context) :UserProfileRepositoryDataSource {
    override fun getUserName(): Resource<String> {
        val userName =  FirebaseAuth.getInstance().currentUser?.displayName

        return if(!userName.isNullOrEmpty()) {
            Resource.Success(userName)
        }else{
            Resource.Error("userName not found")
        }
    }

    override  fun  userLoggingOut(): Resource<String>{
        val task = AuthUI.getInstance().signOut(context)
        Tasks.await(task)
        return if (task.isSuccessful) {
            Resource.Success("Successful user log out ")
        } else {
            val errorMsg = task.exception?.message?:"Not able to logged out successfully "
            Resource.Error(errorMsg)
        }


    }
}