package com.manish.stockapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.manish.stockapp.model.StockDetailsModel
import com.manish.stockapp.network.RetrofitInstance


class StockDetailsRepository {

    suspend fun getStocksDetails() = RetrofitInstance.stockDetailsApi.getStockDetails()


    private val db = FirebaseFirestore.getInstance()
    private val notebookRef = db.collection("StockDB")
    private val noteRef = db.document("StockDB/Stock Details")

    fun saveDataToFavorite(stockDetailsList : List<StockDetailsModel>){

        // todo call in background thread
        for(stock in stockDetailsList){
            Log.d(TAG, "saveDataToFirebase  stock ::  $stock")
            notebookRef.add(stock)
        }

    }

    fun doFavorite(stockDetailsModel : StockDetailsModel){
            notebookRef.add(stockDetailsModel)
    }

    companion object {
        val TAG = "StockDetailsRepository"
    }

}