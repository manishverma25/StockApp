package com.manish.stockapp.data.repository

import android.app.Application
import android.content.Context
import com.hadi.retrofitmvvm.util.Utils.hasInternetConnection
import com.manish.stockapp.api.ApiService
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsApiResponse

import com.manish.stockapp.util.Constants.NO_INTERNET_AVAILABLE
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository  @Inject constructor (private val apiService: ApiService, val context: Context): NetworkRepositoryDataSource {
     override suspend fun getStocksDetails() : Resource<StockDetailsApiResponse> {
          return if (hasInternetConnection(context as Application)) {
               val response = apiService.getStockDetails()
               handleStockDetailsResponse(response)
          } else {
               Resource.Error(NO_INTERNET_AVAILABLE)
          }

     }


     private fun handleStockDetailsResponse(response: Response<StockDetailsApiResponse>): Resource<StockDetailsApiResponse> {
          if (response.isSuccessful) {
               response.body()?.let { resultResponse ->
                    return Resource.Success(resultResponse)
               }
          }
          return Resource.Error(response.message())
     }
}

