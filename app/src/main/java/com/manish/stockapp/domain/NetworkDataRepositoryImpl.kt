package com.manish.stockapp.domain

import com.hadi.retrofitmvvm.util.Utils.hasInternetConnection
import com.manish.stockapp.R
import com.manish.stockapp.StockApplication
import com.manish.stockapp.StockApplication.Companion.appContext
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsApiResponse
import com.manish.stockapp.network.RetrofitInstance
import retrofit2.Response
import javax.inject.Inject

class NetworkDataRepositoryImpl  @Inject constructor (): DataRepositoryUseCase {
     override suspend fun getStocksDetails() : Resource<StockDetailsApiResponse> {
          if (hasInternetConnection(StockApplication.appContext)) {
               val response = RetrofitInstance.apiService.getStockDetails()
               if (response.isSuccessful && response.body() != null) {
                    return Resource.Success(response.body()!!);
               } else {
                    return Resource.Error(appContext.getString(R.string.api_failure))
               }
          } else {
               return Resource.Error(appContext.getString(R.string.no_internet_connection))
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

