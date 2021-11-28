package com.manish.stockapp.data.repository

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.manish.stockapp.data.Error.FIREBASE_USER_DETAILS_NOT_FOUND
import com.manish.stockapp.data.Resource
import javax.inject.Inject

class UserProfileProfileRepository @Inject constructor(val context: Context) :UserProfileRepositoryDataSource {
    override fun getUserName(): Resource<String> {
        val userName =  FirebaseAuth.getInstance().currentUser?.displayName

        return if(!userName.isNullOrEmpty()) {
            Resource.Success(userName)
        }else{
            Resource.Error(FIREBASE_USER_DETAILS_NOT_FOUND)
        }
    }

    override  fun  userLoggingOut(): Resource<String>{
        val task = AuthUI.getInstance().signOut(context)
        Tasks.await(task)
        return if (task.isSuccessful) {
            Resource.Success("")
        } else {
            val errorMsg = task.exception?.message?:"Not able to logged out successfully "
            Resource.Error(errorMsg)
        }


    }
}