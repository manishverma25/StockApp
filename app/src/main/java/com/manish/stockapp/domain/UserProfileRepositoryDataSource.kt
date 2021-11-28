package com.manish.stockapp.domain

import android.content.Context
import com.manish.stockapp.data.Resource

interface UserProfileRepositoryDataSource {
   fun  getUserName(): Resource<String>
    fun  userLoggingOut(): Resource<String>
}