package com.manish.stockapp.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hadi.retrofitmvvm.util.Utils.hasInternetConnection
import com.manish.stockapp.R
import com.manish.stockapp.app.StockApplication
import com.manish.stockapp.model.SearchDetailsResponse
import com.manish.stockapp.model.StockDetailsModel
import com.manish.stockapp.repository.StockDetailsRepository
import com.manish.stockapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class DashboardViewModel( app: StockApplication, private val appRepository: StockDetailsRepository) : AndroidViewModel(app)  {


    val stockDetailLiveData: MutableLiveData<Resource<SearchDetailsResponse>> = MutableLiveData()

    init {

        getStocksDetails()
    }

    private fun getStocksDetails() = viewModelScope.launch {
        fetchStocksDetails()
    }

    private suspend fun fetchStocksDetails() {
        stockDetailLiveData.postValue(Resource.Loading())

        try {
            if (hasInternetConnection(getApplication<StockApplication>())) {
                val response = appRepository.getStocksDetails()
                stockDetailLiveData.postValue(handleStockDetailsResponse(response))
            } else {
                stockDetailLiveData.postValue(Resource.Error(getApplication<StockApplication>().getString(
                    R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> stockDetailLiveData.postValue(
                    Resource.Error(
                        getApplication<StockApplication>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> stockDetailLiveData.postValue(
                    Resource.Error(
                        getApplication<StockApplication>().getString(
                            R.string.fetching_stock_details_api_error_msg
                        )
                    )
                )
            }
        }
    }

    private fun handleStockDetailsResponse(response: Response<SearchDetailsResponse>): Resource<SearchDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    val TAG ="DashboardViewModel"
}