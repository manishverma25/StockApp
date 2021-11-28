package com.manish.stockapp.ui.profile

import androidx.lifecycle.*
import com.explore.repos.demoapplication.CoroutineContextProvider
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.repository.UserProfileRepositoryDataSource
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProfileViewModel @Inject constructor (
    private val userProfileRepository: UserProfileRepositoryDataSource,
    private val coroutineContextProvider: CoroutineContextProvider
) :ViewModel() {

    val ioContext: CoroutineContext = (coroutineContextProvider.IO)


    private val _userNameLiveData: MutableLiveData<Resource<String>> = MutableLiveData()
    val userNameLiveData: LiveData<Resource<String>> = _userNameLiveData

    private val _signOutSuccessStatusLivaData: MutableLiveData<Resource<String>> = MutableLiveData()
    val signOutSuccessStatusLivaData: LiveData<Resource<String>> = _signOutSuccessStatusLivaData


     fun fetchUserName(){
        viewModelScope.launch(ioContext) {
            _userNameLiveData.postValue(userProfileRepository.getUserName())
        }
    }

    fun userLoggingOut() {
        viewModelScope.launch(ioContext) {
            _signOutSuccessStatusLivaData.postValue(userProfileRepository.
            userLoggingOut())
        }
    }

}