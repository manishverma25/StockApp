package com.manish.stockapp.domain

import com.manish.stockapp.data.StockDetailsApiResponse
import com.manish.stockapp.network.RetrofitInstance
import retrofit2.Response

class NetworkDataRepositoryImpl : DataRepositoryUseCase {
     override suspend fun getStocksDetails() :Response<StockDetailsApiResponse> {
          return RetrofitInstance.apiService.getStockDetails()
     }
}