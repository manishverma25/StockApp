package com.manish.stockapp.data


data class StockDetailsApiResponse (
    val success :Boolean,
    val data : ArrayList<StockDetailsItem>
)