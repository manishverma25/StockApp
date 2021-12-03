package com.manish.stockapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explore.repos.demoapplication.CoroutineContextProvider
import com.manish.stockapp.data.*
import com.manish.stockapp.data.repository.NetworkRepositoryDataSource
import com.manish.stockapp.data.repository.FavoriteRepositoryDataSource
import com.manish.stockapp.util.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeViewModel @Inject constructor(
    private val networkNetworkRepositoryDataSourceImpl: NetworkRepositoryDataSource,
    private val favoriteRepositoryImpl: FavoriteRepositoryDataSource,
    private val coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {


    private val _stocksDetailApiStatusLiveData: MutableLiveData<Resource<StockDetailsApiResponse>> =
        MutableLiveData()
    val stocksDetailApiStatusLiveData: LiveData<Resource<StockDetailsApiResponse>> =
        _stocksDetailApiStatusLiveData


    private val ioContext: CoroutineContext = (coroutineContextProvider.IO)

    private val _stockDetailsApiTrackingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val stockDetailsApiTrackingLiveData: LiveData<Boolean> = _stockDetailsApiTrackingLiveData

    private val _favoriteStatusLiveData: MutableLiveData<Resource<String>> = MutableLiveData()

    val favoriteStatusLiveData: LiveData<Resource<String>>
        get() {
            return _favoriteStatusLiveData
        }


    fun fetchStockDetailsData() = viewModelScope.launch(ioContext) {
        _stockDetailsApiTrackingLiveData.postValue(true)
        _stocksDetailApiStatusLiveData.postValue(Resource.Loading())
        val response = networkNetworkRepositoryDataSourceImpl.getStocksDetails()
        _stocksDetailApiStatusLiveData.postValue(response)
    }

    //Flow
    fun fetchStockDetailsDataUsingFlow() = viewModelScope.launch(ioContext) {
        val latestNews: Flow<List<Resource<StockDetailsApiResponse>>>



    }

    lateinit var  periodicStockDetailsJob :Job

    fun startPeriodicJobForFetchingStocksDetails(){
               periodicStockDetailsJob = viewModelScope.launch(ioContext) {
                try {
                    println("launched  job")
                    fetchStockDetailsData();
                    delay(Constants.STOCK_DEATILS_API_PERIODIC_TIMER)
                    ensureActive()
                    if(isActive ){
                        repeat(1, {startPeriodicJobForFetchingStocksDetails()})
                    }

                } catch (e: CancellationException) {
                    println("cancel job  $isActive")
                }
            }
    }

    fun  stopPeriodicJobForFetchingStocksDetails(){

        Log.d(TAG,"stopPeriodicStcokDetailsApiCall ...  ")
        periodicStockDetailsJob.cancel()
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