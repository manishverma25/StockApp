package com.manish.stockapp

import android.app.Application
import com.manish.stockapp.di.component.AppComponent
import com.manish.stockapp.di.component.DaggerAppComponent

class StockApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        appContext = this
        initDI()
    }

      companion object {

       lateinit  var appContext: StockApplication

       lateinit var appComponent: AppComponent

    }

    private fun initDI() {
        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)
    }

}