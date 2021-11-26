package com.manish.stockapp.domain

import com.manish.stockapp.data.StockDetailsApiResponse
import com.manish.stockapp.network.RetrofitInstance
import retrofit2.Response
import javax.inject.Inject

class NetworkDataRepositoryImpl  @Inject constructor (): DataRepositoryUseCase {
     override suspend fun getStocksDetails() :Response<StockDetailsApiResponse> {
          return RetrofitInstance.apiService.getStockDetails()
     }
}