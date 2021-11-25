package com.manish.stockapp.viewmodel

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hadi.retrofitmvvm.util.Utils.hasInternetConnection
import com.manish.stockapp.R
import com.manish.stockapp.app.StockApplication
import com.manish.stockapp.model.SearchDetailsResponse
import com.manish.stockapp.model.StockDetailsModel
import com.manish.stockapp.repository.StockDetailsRepository
import com.manish.stockapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class DashboardViewModel( app: StockApplication, private val appRepository: StockDetailsRepository) : AndroidViewModel(app)  {


    val stockDetailLiveData: MutableLiveData<Resource<SearchDetailsResponse>> = MutableLiveData()

    val isNeedTpResetSelectedItemListLiveData:MutableLiveData<Boolean> = MutableLiveData()

    init {

        setIsNeedTpResetSelectedItemListLiveData(false)
        getStocksDetails()
    }

    private fun getStocksDetails() = viewModelScope.launch {
        fetchStocksDetails()
    }

    private suspend fun fetchStocksDetails() {
        stockDetailLiveData.postValue(Resource.Loading())

        try {
            if (hasInternetConnection(getApplication<StockApplication>())) {
                val response = appRepository.getStocksDetails()

                val stocksDetailsResponse  = handleStockDetailsResponse(response)
//                saveStocksToFireStore(stocksDetailsResponse.data?.data)
                stockDetailLiveData.postValue(stocksDetailsResponse)
            } else {
                stockDetailLiveData.postValue(Resource.Error(getApplication<StockApplication>().getString(
                    R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> stockDetailLiveData.postValue(
                    Resource.Error(
                        getApplication<StockApplication>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> stockDetailLiveData.postValue(
                    Resource.Error(
                        getApplication<StockApplication>().getString(
                            R.string.fetching_stock_details_api_error_msg
                        )
                    )
                )
            }
        }
    }

    private fun handleStockDetailsResponse(response: Response<SearchDetailsResponse>): Resource<SearchDetailsResponse> {
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

        appRepository.saveDataToFavorite(selectedStockList)
        resetSelectedItemList()
    }

    private fun resetSelectedItemList(){
        isNeedTpResetSelectedItemListLiveData.postValue(true)
    }

    fun getSelectedStockList(allStocksList :ArrayList<StockDetailsModel> ):ArrayList<StockDetailsModel>{
       val selectedStockList =  ArrayList<StockDetailsModel>()
        for(stockDetailsItem in allStocksList){
            if(stockDetailsItem.isSelected){
                selectedStockList.add(stockDetailsItem)
            }
        }

        return selectedStockList
    }

    fun setIsNeedTpResetSelectedItemListLiveData(isNeedToReset :Boolean){
        isNeedTpResetSelectedItemListLiveData.postValue(isNeedToReset)
    }





//    private fun saveStocksToFireStore( stockDetailsList : List<StockDetailsModel>? ){
//        if(stockDetailsList == null || stockDetailsList.isEmpty()){
//            //todo update live data that  no stock found
//            return
//        }
//        appRepository.saveDataToFavorite(stockDetailsList)
//
//
//    }




    val TAG ="DashboardViewModel"
}