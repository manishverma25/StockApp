package com.manish.stockapp.ui.home

import androidx.lifecycle.LiveData
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


    val ioContext: CoroutineContext = (coroutineContextProvider.IO)

    val stockDetailsApiHitLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _favoriteStatusLiveData:  MutableLiveData<Resource<Any>> = MutableLiveData()

    val favoriteStatusLiveData: LiveData<Resource<Any>>
        get() {
            return _favoriteStatusLiveData
        }


    fun fetchStockDetailsData() = viewModelScope.launch(ioContext) {
        stockDetailsApiHitLiveData.postValue(true)
        stockDetailLiveData.postValue(Resource.Loading())
        val response = networkDataRepositoryUseCaseImpl.getStocksDetails()
        stockDetailLiveData.postValue(response)
    }



    fun doFavorite(stockDetailsItem: StockDetailsItem) {
        viewModelScope.launch(ioContext) {
            val favoriteResponse =    favoriteRepositoryImpl.doFavorite(stockDetailsItem)
            _favoriteStatusLiveData.postValue(favoriteResponse)
        }
    }




    companion object {
        val TAG = "HomeViewModel"
    }
}