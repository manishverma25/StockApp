package com.manish.stockapp.di.module

import androidx.lifecycle.ViewModelProvider
import com.manish.stockapp.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}