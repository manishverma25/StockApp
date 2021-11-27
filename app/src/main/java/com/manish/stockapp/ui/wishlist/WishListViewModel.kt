package com.manish.stockapp.ui.wishlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explore.repos.demoapplication.CoroutineContextProvider
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsItem
import com.manish.stockapp.domain.FavoriteRepositoryUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class WishListViewModel @Inject constructor (
    val favoriteRepositoryImpl: FavoriteRepositoryUseCase,
    val coroutineContextProvider: CoroutineContextProvider
) :
    ViewModel() {

//    val favoriteStockListLiveData : MutableLiveData<List<StockDetailsItem>> = MutableLiveData()

    val ioContext: CoroutineContext = (coroutineContextProvider.IO)

//    val wishListSnapShotListenerErrorLiveData : MutableLiveData<String> = MutableLiveData()

    private val _WishListViewModelStateLiveData: MutableLiveData<WishListViewModelState> = MutableLiveData()

    val wishListViewModelStateLiveData: LiveData<WishListViewModelState>
        get() {
            return _WishListViewModelStateLiveData
        }

    fun fetchFavoriteStocksList() { //LiveData<List<StockDetailsItem>>

        var wishListViewModelState: WishListViewModelState
        viewModelScope.launch(ioContext) {
            favoriteRepositoryImpl.getAllSavedStockCollection()
                .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
//                    favoriteStockListLiveData.value = null
//                    wishListSnapShotListenerErrorLiveData.postValue(e.message)
                        wishListViewModelState =  transformToState(Resource.Error(e.message ?: ""))
                        _WishListViewModelStateLiveData.postValue(wishListViewModelState)
                        return@EventListener
                    }

                    var savedAddressList: MutableList<StockDetailsItem> = mutableListOf()
                    for (doc in value!!) {
                        var stockDetailsItem = doc.toObject(StockDetailsItem::class.java)
                        Log.w(TAG, "stockDetailsItem  :  $stockDetailsItem")
                        if (stockDetailsItem.isFavorite) {
                            savedAddressList.add(stockDetailsItem)
                        }
                    }
                    wishListViewModelState = transformToState(Resource.Success(savedAddressList))
                    _WishListViewModelStateLiveData.postValue(wishListViewModelState)
//            favoriteStockListLiveData.postValue(savedAddressList)
                })
        }
//        return favoriteStockListLiveData
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