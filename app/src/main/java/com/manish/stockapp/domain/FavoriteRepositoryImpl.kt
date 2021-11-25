package com.manish.stockapp.domain

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.manish.stockapp.data.StockDetailsItem
import com.manish.stockapp.util.Constants.FIREBASE_COLLECTION_PATH


class FavoriteRepositoryImpl : FavoriteRepositoryUseCase {


    private val db = FirebaseFirestore.getInstance()
    private val wishlistStockCollectionRef = db.collection(FIREBASE_COLLECTION_PATH)
    private val wishlistStockDocumentRef =
        db.document("StockDB/Stock Details")  // db.document(FIREBASE_DOCUMENT_PATH)


    override fun doFavorite(stockDetailsList: List<StockDetailsItem>) {
        for (stock in stockDetailsList) {
            Log.d(TAG, "saveDataToFirebase  stock ::  $stock")
            wishlistStockCollectionRef.add(stock)
        }
    }


    fun doAllUnFavorite() {
        wishlistStockDocumentRef.delete()

    }

    companion object {
        val TAG = "StockDetailsRepository"
    }

}