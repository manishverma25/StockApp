package com.manish.stockapp.repository

import com.manish.stockapp.network.RetrofitInstance

class StockDetailsRepository {

    suspend fun getPictures() = RetrofitInstance.stockDetailsApi.getStockDetails()

}