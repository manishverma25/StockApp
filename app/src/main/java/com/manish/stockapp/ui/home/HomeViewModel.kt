package com.manish.stockapp.ui.home

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class HomeViewModel @Inject constructor (val networkDataRepositoryUseCaseImpl: NetworkDataRepositoryImpl, val favoriteRepositoryImpl: FavoriteRepositoryImpl) :
    ViewModel() {


    val stockDetailLiveData: MutableLiveData<Resource<StockDetailsApiResponse>> = MutableLiveData()

    val isNeedToResetSelectedItemListLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val wishlistStockCollectionRef = firebaseFirestore.collection(FIREBASE_COLLECTION_PATH)


    val alreadyWishListStockDetailsList = ArrayList<StockDetailsItem>()

    init {

        setIsNeedTpResetSelectedItemListLiveData(false)
        getStocksDetails()
        initStockListner()
    }

    private fun initStockListner() {
        wishlistStockCollectionRef.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                queryDocumentSnapshots: QuerySnapshot?,
                e: FirebaseFirestoreException?
            ) {
                if (e != null) {
                    return
                }
                if (queryDocumentSnapshots == null) {
                    Log.d(TAG, "No record foudn in firebase : ")
                    return
                }
                alreadyWishListStockDetailsList.clear();
                for (documentSnapshot in queryDocumentSnapshots) {
                    val stockDetailsItem: StockDetailsItem =
                        documentSnapshot.toObject(StockDetailsItem::class.java)
//                    stockDetailsModel.setDocumentId(documentSnapshot.id)
                    Log.d(TAG, "adding already stockDetailsModel : " + stockDetailsItem.sid)
                    alreadyWishListStockDetailsList.add(stockDetailsItem)
                }
            }
        })
    }



    private fun getStocksDetails() = viewModelScope.launch {
        fetchStocksDetails()
    }

    private suspend fun fetchStocksDetails() {
        stockDetailLiveData.postValue(Resource.Loading())

        try {
            if (hasInternetConnection(StockApplication.appContext  )) {
                val response = networkDataRepositoryUseCaseImpl.getStocksDetails()

                val stocksDetailsResponse = handleStockDetailsResponse(response)
//                saveStocksToFireStore(stocksDetailsResponse.data?.data)
                stockDetailLiveData.postValue(stocksDetailsResponse)
            } else {
                stockDetailLiveData.postValue(
                    Resource.Error(
                        StockApplication.appContext.getString(
                            R.string.no_internet_connection
                        )
                    )
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> stockDetailLiveData.postValue(
                    Resource.Error(
                        StockApplication.appContext .getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> stockDetailLiveData.postValue(
                    Resource.Error(
                        StockApplication.appContext .getString(
                            R.string.fetching_stock_details_api_error_msg
                        )
                    )
                )
            }
        }
    }

    private fun handleStockDetailsResponse(response: Response<StockDetailsApiResponse>): Resource<StockDetailsApiResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    fun doSaveFavorite() {
        Log.d(TAG, "doSaveFavorite ......")

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
        Log.d(TAG, "doALlUnFavorite ......")
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


    val TAG = "DashboardViewModel"
}