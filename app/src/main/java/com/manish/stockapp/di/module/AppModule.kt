package com.manish.stockapp.di.module

import com.manish.stockapp.domain.DataRepositoryUseCase
import com.manish.stockapp.domain.FavoriteRepositoryImpl
import com.manish.stockapp.domain.FavoriteRepositoryUseCase
import com.manish.stockapp.domain.NetworkDataRepositoryImpl
import dagger.Binds
import dagger.Module


@Module
abstract class AppModule {


    @Binds
    abstract fun bindsFavoriteRepositoryUseCase(favoriteRepositoryImpl: FavoriteRepositoryImpl): FavoriteRepositoryUseCase

    @Binds
    abstract fun bindsDataRepositoryUseCase(networkDataRepositoryImpl: NetworkDataRepositoryImpl): DataRepositoryUseCase


}