package com.manish.stockapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.manish.stockapp.model.StockDetailsModel
import com.manish.stockapp.network.RetrofitInstance
import com.manish.stockapp.util.Constants.FIREBASE_COLLECTION_PATH
import com.manish.stockapp.util.Constants.FIREBASE_DOCUMENT_PATH
import com.manish.stockapp.util.Constants.KEY_FIELD_FOR_FAVORITE


class StockDetailsRepository {

    suspend fun getStocksDetails() = RetrofitInstance.stockDetailsApi.getStockDetails()

    private val db = FirebaseFirestore.getInstance()
    private val wishlistStockCollectionRef = db.collection(FIREBASE_COLLECTION_PATH)
    private val wishlistStockDocumentRef =
        db.document("StockDB/Stock Details")  // db.document(FIREBASE_DOCUMENT_PATH)


    fun saveDataToFavorite(stockDetailsList: List<StockDetailsModel>) {

        for (stock in stockDetailsList) {
            Log.d(TAG, "saveDataToFirebase  stock ::  $stock")
            wishlistStockCollectionRef.add(stock)
        }

    }

    fun doAllUnFavorite(stockDetailsList: List<StockDetailsModel>) {
        wishlistStockDocumentRef.delete()

    }

    fun doFavorite(stockDetailsModel: StockDetailsModel) {
        wishlistStockCollectionRef.add(stockDetailsModel)
    }

    companion object {
        val TAG = "StockDetailsRepository"
    }

}