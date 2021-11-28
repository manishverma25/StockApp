package com.manish.stockapp.domain

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.manish.stockapp.data.StockDetailsItem
import com.manish.stockapp.util.Constants.FIREBASE_COLLECTION_PATH
import com.manish.stockapp.util.Constants.FIREBASE_DOCUMENT_NAME
import java.lang.Exception
//import com.manish.stockapp.util.Constants.FIREBASE_DOCUMENT_PATH
import javax.inject.Inject
import com.manish.stockapp.data.FavoriteStockDetails
import com.manish.stockapp.data.Resource


class FavoriteRepository  @Inject constructor (): FavoriteRepositoryDataSource {



    private val fireStoreDB = FirebaseFirestore.getInstance()
    private val fireStoreCollection = fireStoreDB.collection(FIREBASE_COLLECTION_PATH)

    override fun doFavorite(stockDetailItem: StockDetailsItem): Resource<String> {
        Log.d(TAG, "doFavorite  stock ::  $stockDetailItem")
        val favoriteItem = FavoriteStockDetails(stockDetailItem.sid, true)
        val email = FirebaseAuth.getInstance().currentUser?.email
        if(email.isNullOrEmpty()){
            return  Resource.Error("user details not found ")
        }
        val documentReference = fireStoreCollection.document(email.toString())
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


    override fun getFavoriteStocksCollection(): Resource<List<StockDetailsItem>>? {
        val favoriteResponse : Resource<List<StockDetailsItem>>? =   Resource.Error(message = "",null)
        val email = FirebaseAuth.getInstance().currentUser?.email
        if(email.isNullOrEmpty()){
            return  Resource.Error("user details not found ")
        }
        val collectionReference =  fireStoreDB.collection(FIREBASE_COLLECTION_PATH + "/${email}/" + FIREBASE_DOCUMENT_NAME)
        val task = collectionReference.get()
        Tasks.await(task)
        /***
         * Refered this use task   Tasks.await(task) , and remove snapshotLisnte
         * https://medium.com/firebase-tips-tricks/how-to-read-data-from-cloud-firestore-using-get-bf03b6ee4953
         */
        if (task.isSuccessful){
            val querySnapshot =  task.result
            val savedAddressList: MutableList<StockDetailsItem> = mutableListOf()
            if(!querySnapshot?.documents.isNullOrEmpty() ){

                for (doc in querySnapshot?.documents!!.iterator()) {
                        val favoriteStockItem = doc.toObject(FavoriteStockDetails::class.java)
                        Log.w(TAG, "stockDetailsItem  :::: :  $favoriteStockItem")
                        if (favoriteStockItem?.isfavorite == true) {
                            savedAddressList.add(StockDetailsItem(favoriteStockItem.sid))
                        }
                    }

            }
            return  Resource.Success(savedAddressList)
        }
        return favoriteResponse
    }




    companion object {
        val TAG = "FavoriteRepositoryImpl"
    }


}