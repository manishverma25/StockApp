package com.manish.stockapp.ui.profile

import androidx.lifecycle.*
import com.explore.repos.demoapplication.CoroutineContextProvider
import com.manish.stockapp.data.Resource
import com.manish.stockapp.domain.UserProfileRepositoryDataSource
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProfileViewModel @Inject constructor (
    val userProfileRepository: UserProfileRepositoryDataSource,
    val coroutineContextProvider: CoroutineContextProvider
) :ViewModel() {

    val ioContext: CoroutineContext = (coroutineContextProvider.IO)



    private val _userNameLiveData: MutableLiveData<Resource<String>> = MutableLiveData()
    val userNameLiveData: LiveData<Resource<String>> = _userNameLiveData

    private val _signOutSuccessStatusLivaData: MutableLiveData<Resource<String>> = MutableLiveData()
    val signOutSuccessStatusLivaData: LiveData<Resource<String>> = _signOutSuccessStatusLivaData

//    init {
//        fetchUserName()
//
//    }

     fun fetchUserName(){
        viewModelScope.launch(ioContext) {
            _userNameLiveData.postValue(userProfileRepository.getUserName())
        }
    }

    fun userLoggingOut() {
        viewModelScope.launch(ioContext) {
            _signOutSuccessStatusLivaData.postValue(userProfileRepository.userLoggingOut())
        }
    }

}