package com.manish.stockapp.domain

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.manish.stockapp.data.StockDetailsItem
import com.manish.stockapp.util.Constants.FIREBASE_COLLECTION_PATH
import com.manish.stockapp.util.Constants.FIREBASE_DOCUMENT_NAME
//import com.manish.stockapp.util.Constants.FIREBASE_DOCUMENT_PATH
import javax.inject.Inject


class FavoriteRepositoryImpl  @Inject constructor (): FavoriteRepositoryUseCase {

    var user = FirebaseAuth.getInstance().currentUser

    private val fireStoreDB =  FirebaseFirestore.getInstance()
    private val fireStoreCollection = fireStoreDB.collection(FIREBASE_COLLECTION_PATH)



    override fun doFavorite(stockDetailsList: List<StockDetailsItem>) {
        for (stock in stockDetailsList) {
            Log.d(TAG, "saveDataToFirebase  stock ::  $stock")

            stock.isFavorite = true
            val documentReference = fireStoreCollection.document(user!!.email.toString())
                .collection(FIREBASE_DOCUMENT_NAME).document(stock.sid)
            Log.d(TAG, "documentReference   ::  $documentReference")
             val resultTask =  documentReference.set(stock)

//            Log.d(TAG, "resultTask  ::  ${resultTask.result}")
//            Log.d(TAG, "resultTask  ::  ${resultTask.isSuccessful}")

        }
    }


    override fun getAllSavedStockCollection(): CollectionReference {
     var savedStcoksCollectionReference = fireStoreDB.collection(FIREBASE_COLLECTION_PATH+"/${user!!.email.toString()}/"+FIREBASE_DOCUMENT_NAME)
        return savedStcoksCollectionReference
    }

    companion object {
        val TAG = "FavoriteRepositoryImpl"
    }

}