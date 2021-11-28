package com.manish.stockapp.domain

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsItem

interface FavoriteRepositoryDataSource {
    fun doFavorite(stockDetailItem: StockDetailsItem) : Resource<String>

    fun getFavoriteStocksCollection(): Resource<List<StockDetailsItem>>?
}