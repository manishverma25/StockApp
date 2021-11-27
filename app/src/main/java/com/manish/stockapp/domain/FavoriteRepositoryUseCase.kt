package com.manish.stockapp.domain

import com.google.firebase.firestore.CollectionReference
import com.manish.stockapp.data.StockDetailsItem

interface FavoriteRepositoryUseCase { //rename it forebase rep[sositiry
    fun doFavorite(stockDetailsList: List<StockDetailsItem>)

    fun getAllSavedStockCollection(): CollectionReference
}