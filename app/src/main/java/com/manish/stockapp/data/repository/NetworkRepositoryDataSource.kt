package com.manish.stockapp.data.repository
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsApiResponse

interface NetworkRepositoryDataSource {
    suspend fun getStocksDetails() : Resource<StockDetailsApiResponse>
}