package com.manish.stockapp.api

import com.manish.stockapp.data.StockDetailsApiResponse
import retrofit2.Response
import retrofit2.http.GET


interface ApiService {

    @GET("stocks/quotes?sids=RELI%2CTCS%2CITC%2CHDBK%2CINFY")
    suspend fun getStockDetails(): Response<StockDetailsApiResponse>

}