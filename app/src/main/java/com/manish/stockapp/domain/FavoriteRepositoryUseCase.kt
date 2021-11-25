package com.manish.stockapp.domain

import com.manish.stockapp.data.StockDetailsItem

interface FavoriteRepositoryUseCase {
    fun doFavorite(stockDetailsList: List<StockDetailsItem>)
}