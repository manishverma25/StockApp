package com.manish.stockapp.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explore.repos.demoapplication.CoroutineContextProvider
import com.manish.stockapp.data.*
import com.manish.stockapp.domain.DataRepositoryUseCase
import com.manish.stockapp.domain.FavoriteRepositoryUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeViewModel @Inject constructor(
    val networkDataRepositoryUseCaseImpl: DataRepositoryUseCase,
    val favoriteRepositoryImpl: FavoriteRepositoryUseCase,
    val coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {

    val stockDetailLiveData: MutableLiveData<Resource<StockDetailsApiResponse>> = MutableLiveData()

    val isNeedToResetSelectedItemListLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    val ioContext: CoroutineContext = (coroutineContextProvider.IO)

    val stockDetailsApiHitLiveData: MutableLiveData<Boolean> = MutableLiveData(false)


    fun getStocksData() = viewModelScope.launch(ioContext) {
        stockDetailsApiHitLiveData.postValue(true)
        fetchStocksDetails()
    }

    suspend fun fetchStocksDetails() {
        stockDetailLiveData.postValue(Resource.Loading())
        val response = networkDataRepositoryUseCaseImpl.getStocksDetails()
        stockDetailLiveData.postValue(response)
    }


    fun doFavorite() {
        Log.d(Companion.TAG, "doFavorite ......")
        val allStockDetailsList = stockDetailLiveData.value?.data?.data
        if (allStockDetailsList.isNullOrEmpty()) {
            //todo update live data that  no stock found
            return
        }
        val selectedStockList = getSelectedStockList(allStockDetailsList)
        favoriteRepositoryImpl.doFavorite(selectedStockList)
        resetSelectedItemList()
    }


    private fun resetSelectedItemList() {
        isNeedToResetSelectedItemListLiveData.postValue(true)
    }

    fun getSelectedStockList(allStocksList: ArrayList<StockDetailsItem>): ArrayList<StockDetailsItem> {
        val selectedStockList = ArrayList<StockDetailsItem>()
        for (stockDetailsItem in allStocksList) {
            if (stockDetailsItem.isSelected) {
                selectedStockList.add(stockDetailsItem)
            }
        }
        return selectedStockList
    }

    fun setIsNeedTpResetSelectedItemListLiveData(isNeedToReset: Boolean) {
        isNeedToResetSelectedItemListLiveData.postValue(isNeedToReset)
    }


    companion object {
        val TAG = "HomeViewModel"
    }
}