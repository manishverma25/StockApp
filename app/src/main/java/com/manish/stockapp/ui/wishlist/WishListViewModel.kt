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
    private val favoriteRepositoryImpl: FavoriteRepositoryDataSource,
     coroutineContextProvider: CoroutineContextProvider
) :
    ViewModel() {


    val ioContext: CoroutineContext = (coroutineContextProvider.IO)


    private val _wishListStocksListLiveData: MutableLiveData<WishListViewModelState> = MutableLiveData()

    val wishListStocksListLiveData: LiveData<WishListViewModelState>
        get() {
            return _wishListStocksListLiveData
        }

    fun fetchFavoriteStocksList() {

        viewModelScope.launch(ioContext) {
            _wishListStocksListLiveData.postValue(WishListViewModelState.Loading)
            val favoriteListResponse = favoriteRepositoryImpl.getFavoriteStocksCollection()
            _wishListStocksListLiveData.postValue(transformToState(favoriteListResponse))
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