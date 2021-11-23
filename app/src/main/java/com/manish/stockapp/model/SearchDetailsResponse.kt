package com.manish.stockapp.model

//class SearchDetailsResponse : ArrayList<StockDetailsModel>()
data class SearchDetailsResponse (

    val success :Boolean,
    val data : ArrayList<StockDetailsModel>
)