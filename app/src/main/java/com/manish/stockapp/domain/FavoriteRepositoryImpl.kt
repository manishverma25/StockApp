package com.manish.stockapp.domain

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.manish.stockapp.data.StockDetailsItem
import com.manish.stockapp.util.Constants.FIREBASE_COLLECTION_PATH
import com.manish.stockapp.util.Constants.FIREBASE_DOCUMENT_NAME
import java.lang.Exception
//import com.manish.stockapp.util.Constants.FIREBASE_DOCUMENT_PATH
import javax.inject.Inject
import com.manish.stockapp.data.FavoriteStockItem
import com.manish.stockapp.data.Resource
import okhttp3.internal.wait


class FavoriteRepositoryImpl  @Inject constructor (): FavoriteRepositoryUseCase {

    var user = FirebaseAuth.getInstance().currentUser

    private val fireStoreDB =  FirebaseFirestore.getInstance()
    private val fireStoreCollection = fireStoreDB.collection(FIREBASE_COLLECTION_PATH)


    /***
     *
     *
     */

    override fun doFavorite(stockDetailItem: StockDetailsItem): Resource<Any> {
        Log.d(TAG, "doFavorite  stock ::  $stockDetailItem")
        val favoriteItem = FavoriteStockItem(stockDetailItem.sid, true)
        val documentReference = fireStoreCollection.document(user!!.email.toString())
            .collection(FIREBASE_DOCUMENT_NAME).document(favoriteItem.sid)
        try {
            val resultTask = documentReference.set(favoriteItem)
               Tasks.await(resultTask)
            return if (resultTask.isSuccessful) {
                Resource.Success("")
            } else {
                Resource.Error("")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error("")
    }


    override fun getFavoriteStocksCollection(): CollectionReference {
     var savedStcoksCollectionReference = fireStoreDB.collection(FIREBASE_COLLECTION_PATH+"/${user!!.email.toString()}/"+FIREBASE_DOCUMENT_NAME)
        return savedStcoksCollectionReference
    }

    companion object {
        val TAG = "FavoriteRepositoryImpl"
    }

}