package com.manish.stockapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manish.stockapp.domain.FavoriteRepository
import com.manish.stockapp.domain.NetworkNetworkRepositoryImpl


// not needed  this class
class ViewModelProviderFactory(
    val app: StockApplication,
    val networkDataRepositoryImpl: NetworkNetworkRepositoryImpl,
    val favoriteRepository: FavoriteRepository
) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
//            return HomeViewModel(networkDataRepositoryImpl, favoriteRepositoryImpl) as T
//        }

        throw IllegalArgumentException("Unknown class name for ViewModelProviderFactory ...")
    }

}