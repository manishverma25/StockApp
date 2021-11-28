package com.manish.stockapp.ui.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explore.repos.demoapplication.CoroutineContextProvider
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsItem
import com.manish.stockapp.domain.FavoriteRepositoryDataSource
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class WishListViewModel @Inject constructor (
    val favoriteRepositoryImpl: FavoriteRepositoryDataSource,
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
            _wishListViewModelStateLiveData.postValue(WishListViewModelState.Loading)
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