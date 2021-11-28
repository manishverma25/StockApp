package com.manish.stockapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explore.repos.demoapplication.CoroutineContextProvider
import com.google.firebase.auth.FirebaseAuth
import com.manish.stockapp.data.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProfileViewModel @Inject constructor (
    val coroutineContextProvider: CoroutineContextProvider
) :ViewModel() {

    val ioContext: CoroutineContext = (coroutineContextProvider.IO)



    private val _userNameLiveData: MutableLiveData<Resource<String>> = MutableLiveData()
    val userNameLiveData: LiveData<Resource<String>> = _userNameLiveData




    init {
        setUserName()

    }

    private fun setUserName(){
        viewModelScope.launch(ioContext) {
            val userName =  FirebaseAuth.getInstance().currentUser?.displayName
            _userNameLiveData.postValue(Resource.Success (userName?:""))
        }
    }


}