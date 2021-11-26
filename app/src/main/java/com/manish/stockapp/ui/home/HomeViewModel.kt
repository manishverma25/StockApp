package com.manish.stockapp.ui.home

import android.app.Application
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explore.repos.demoapplication.CoroutineContextProvider
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.hadi.retrofitmvvm.util.Utils.hasInternetConnection
import com.manish.stockapp.R
import com.manish.stockapp.StockApplication
import com.manish.stockapp.data.*
import com.manish.stockapp.domain.DataRepositoryUseCase
import com.manish.stockapp.domain.FavoriteRepositoryImpl
import com.manish.stockapp.domain.FavoriteRepositoryUseCase
import com.manish.stockapp.domain.NetworkDataRepositoryImpl
import com.manish.stockapp.util.Constants.FIREBASE_COLLECTION_PATH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeViewModel @Inject constructor (
     val networkDataRepositoryUseCaseImpl: DataRepositoryUseCase,
                                         val favoriteRepositoryImpl: FavoriteRepositoryUseCase,
                                         val coroutineContextProvider: CoroutineContextProvider
) :
    ViewModel() {


    val stockDetailLiveData: MutableLiveData<Resource<StockDetailsApiResponse>> = MutableLiveData()

    val isNeedToResetSelectedItemListLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
//    private val firebaseFirestore = FirebaseFirestore.getInstance()
//    private val wishlistStockCollectionRef = firebaseFirestore.collection(FIREBASE_COLLECTION_PATH)

    val ioContext: CoroutineContext = (coroutineContextProvider.IO)


    val alreadyWishListStockDetailsList = ArrayList<StockDetailsItem>()

    val stockDetailsApiHitLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        initStockListener()
    }




    private fun initStockListener() {
        viewModelScope.launch(ioContext) {
            FirebaseFirestore.getInstance().collection(FIREBASE_COLLECTION_PATH).addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    queryDocumentSnapshots: QuerySnapshot?,
                    e: FirebaseFirestoreException?
                ) {
                    if (e != null) {
                        return
                    }
                    if (queryDocumentSnapshots == null) {
                        Log.d(Companion.TAG, "No record foudn in firebase : ")
                        return
                    }
                    alreadyWishListStockDetailsList.clear();
                    for (documentSnapshot in queryDocumentSnapshots) {
                        val stockDetailsItem: StockDetailsItem =
                            documentSnapshot.toObject(StockDetailsItem::class.java)
                        Log.d(Companion.TAG, "adding already stockDetailsModel : " + stockDetailsItem.sid)
                        alreadyWishListStockDetailsList.add(stockDetailsItem)
                    }
                }
            })
        }

    }



     fun getStocksData() = viewModelScope.launch (ioContext) {
         stockDetailsApiHitLiveData.postValue(true)
         fetchStocksDetails()
    }

    suspend fun fetchStocksDetails() {
        stockDetailLiveData.postValue(Resource.Loading())
        val response = networkDataRepositoryUseCaseImpl.getStocksDetails()
        stockDetailLiveData.postValue(response)
    }




    fun doSaveFavorite() {
        Log.d(Companion.TAG, "doSaveFavorite ......")

        val allStockDetailsList = stockDetailLiveData.value?.data?.data
        if (allStockDetailsList.isNullOrEmpty()) {
            //todo update live data that  no stock found
            return
        }
        val selectedStockList = getSelectedStockList(allStockDetailsList)


        if (selectedStockList.isNullOrEmpty()) {
            //todo update live data that  no item  selected //or it can be done by menu item disable
            return
        }
        // todo call in background thread


        val stockDetailsWithoutDuplicacyList =
            selectedStockList.minus(alreadyWishListStockDetailsList)
        if(stockDetailsWithoutDuplicacyList .isNullOrEmpty()){
            favoriteRepositoryImpl.doFavorite(stockDetailsWithoutDuplicacyList)
        }else{
            // show msg stock  already   in wishj list
        }

        resetSelectedItemList()
    }


    private fun resetSelectedItemList() {
        isNeedToResetSelectedItemListLiveData.postValue(true)
    }

    fun getSelectedStockList(allStocksList: ArrayList<StockDetailsItem>): ArrayList<StockDetailsItem> {
        val selectedStockList = ArrayList<StockDetailsItem>()
        for (stockDetailsItem in allStocksList) {
            if (stockDetailsItem.isSelected) {
                selectedStockList.add(stockDetailsItem)
            }
        }
        return selectedStockList
    }

    fun setIsNeedTpResetSelectedItemListLiveData(isNeedToReset: Boolean) {
        isNeedToResetSelectedItemListLiveData.postValue(isNeedToReset)
    }

    // doAllUnFavorite is for dev for purpose

    fun doAllUnFavorite() {
        Log.d(Companion.TAG, "doALlUnFavorite ......")
        val allStockDetailsList = stockDetailLiveData.value?.data?.data
        if (allStockDetailsList.isNullOrEmpty()) {
            return
        }
        val selectedStockList = getSelectedStockList(allStockDetailsList)
        if (selectedStockList.isNullOrEmpty()) {
            return
        }
//        favoriteRepositoryImpl.doAllUnFavorite()
        resetSelectedItemList()
    }


    companion object {
        val TAG = "HomeViewModel"
    }
}