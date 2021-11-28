package com.manish.stockapp.data.repository

import com.manish.stockapp.data.FavoriteStockDetails
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsItem

interface FavoriteRepositoryDataSource {
    fun doFavorite(stockDetailItem: StockDetailsItem) : Resource<String>

    fun getFavoriteStocksCollection(): Resource<List<FavoriteStockDetails>>?
}