package com.manish.stockapp.domain
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsApiResponse

interface DataRepositoryUseCase {
    suspend fun getStocksDetails() : Resource<StockDetailsApiResponse>
}