package com.manish.stockapp.ui.wishlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explore.repos.demoapplication.CoroutineContextProvider
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.manish.stockapp.data.FavoriteStockItem
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsItem
import com.manish.stockapp.domain.FavoriteRepositoryUseCase
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class WishListViewModel @Inject constructor (
    val favoriteRepositoryImpl: FavoriteRepositoryUseCase,
    val coroutineContextProvider: CoroutineContextProvider
) :
    ViewModel() {


    val ioContext: CoroutineContext = (coroutineContextProvider.IO)


    private val _wishListViewModelStateLiveData: MutableLiveData<WishListViewModelState> = MutableLiveData()

    val wishListViewModelStateLiveData: LiveData<WishListViewModelState>
        get() {
            return _wishListViewModelStateLiveData
        }

    fun fetchFavoriteStocksList() { //LiveData<List<StockDetailsItem>>

        viewModelScope.launch(ioContext) {
            val favoriteListResponse = favoriteRepositoryImpl.getFavoriteStocksCollection()
            _wishListViewModelStateLiveData.postValue(transformToState(favoriteListResponse))
        }
    }

    private fun transformToState(resource: Resource<List<StockDetailsItem>>?): WishListViewModelState {
        return when(resource){
            is Resource.Success -> {
                WishListViewModelState.Success(resource.data as List<StockDetailsItem> )
            }
            is Resource.Error -> {
                WishListViewModelState.Error(resource.message?:"")
            }
            is Resource.Loading -> {
                WishListViewModelState.Loading
            }
            else -> WishListViewModelState.Loading
        }
    }

    companion object {
        val TAG = "WishListViewModel"
    }
}


sealed class WishListViewModelState{
    object Loading: WishListViewModelState()
    data class Success(val data: List<StockDetailsItem>): WishListViewModelState()
    data class Error(val errorMessage: String): WishListViewModelState()

}