package com.manish.stockapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manish.stockapp.app.StockApplication
import com.manish.stockapp.repository.StockDetailsRepository


class ViewModelProviderFactory(
    val app: StockApplication,
    val appRepository: StockDetailsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(app, appRepository) as T
        }

        throw IllegalArgumentException("Unknown class name for ViewModelProviderFactory ...")
    }

}