package com.manish.stockapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.manish.stockapp.model.StockDetailsModel
import com.manish.stockapp.network.RetrofitInstance
import com.manish.stockapp.util.Constants.FIREBASE_COLLECTION_PATH
import com.manish.stockapp.util.Constants.FIREBASE_DOCUMENT_PATH


class StockDetailsRepository {

    suspend fun getStocksDetails() = RetrofitInstance.stockDetailsApi.getStockDetails()

    private val db = FirebaseFirestore.getInstance()
    private val wishlistStockDetailRef = db.collection(FIREBASE_COLLECTION_PATH)
    private val wishlistStockDocument = db.document(FIREBASE_DOCUMENT_PATH)

    fun saveDataToFavorite(stockDetailsList : List<StockDetailsModel>){

        // todo call in background thread
        for(stock in stockDetailsList){
            Log.d(TAG, "saveDataToFirebase  stock ::  $stock")
            wishlistStockDetailRef.add(stock)
        }

    }

    fun doFavorite(stockDetailsModel : StockDetailsModel){
            wishlistStockDetailRef.add(stockDetailsModel)
    }

    companion object {
        val TAG = "StockDetailsRepository"
    }

}