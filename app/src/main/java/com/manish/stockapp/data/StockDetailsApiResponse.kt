package com.manish.stockapp.data

import com.manish.stockapp.data.StockDetailsItem

//class SearchDetailsResponse : ArrayList<StockDetailsModel>()
data class StockDetailsApiResponse (

    val success :Boolean,
    val data : ArrayList<StockDetailsItem>
)