package com.manish.stockapp.data.repository

import com.manish.stockapp.data.Resource

interface UserProfileRepositoryDataSource {
   fun  getUserName(): Resource<String>
    fun  userLoggingOut(): Resource<String>
}