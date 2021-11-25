package com.manish.stockapp

import android.app.Application
import com.manish.stockapp.di.component.AppComponent
import com.manish.stockapp.di.component.DaggerAppComponent

class StockApplication : Application() {
    val appComponent : AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}