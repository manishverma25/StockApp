package com.manish.stockapp.network

import com.manish.stockapp.model.SearchDetailsResponse
import com.manish.stockapp.model.StockDetailsModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface API {

    @GET("stocks/quotes?sids=RELI%2CTCS%2CITC%2CHDBK%2CINFY") //todo mvg
    suspend fun getStockDetails(): Response<SearchDetailsResponse>

}