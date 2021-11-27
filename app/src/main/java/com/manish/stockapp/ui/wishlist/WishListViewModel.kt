package com.manish.stockapp.ui.wishlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explore.repos.demoapplication.CoroutineContextProvider
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
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

    val favoriteStockListLiveData : MutableLiveData<List<StockDetailsItem>> = MutableLiveData()

    val ioContext: CoroutineContext = (coroutineContextProvider.IO)

    val wishListSnapShotListenerErrorLiveData : MutableLiveData<String> = MutableLiveData()


    fun getFavoriteStockListLiveData(): LiveData<List<StockDetailsItem>> {

        viewModelScope.launch(ioContext) {
            favoriteRepositoryImpl.getAllSavedStockCollection().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    favoriteStockListLiveData.value = null
                    wishListSnapShotListenerErrorLiveData.postValue(e.message)
                    return@EventListener
                }

                var savedAddressList : MutableList<StockDetailsItem> = mutableListOf()
                for (doc in value!!) {
                    var stockDetailsItem = doc.toObject(StockDetailsItem::class.java)
                    Log.w(TAG, "stockDetailsItem  :  $stockDetailsItem" )
                    if(stockDetailsItem.isFavorite){
                        savedAddressList.add(stockDetailsItem)
                    }
                }
            favoriteStockListLiveData.postValue(savedAddressList)
            })
        }
        return favoriteStockListLiveData
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