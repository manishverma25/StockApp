package com.manish.stockapp.domain
import com.manish.stockapp.data.StockDetailsApiResponse
import retrofit2.Response

interface DataRepositoryUseCase {
    suspend fun getStocksDetails() :Response<StockDetailsApiResponse>
}