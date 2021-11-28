package com.manish.stockapp.di.module

import android.app.Application
import android.content.Context
import com.manish.stockapp.domain.*
import dagger.Binds
import dagger.Module


@Module
abstract class AppModule {



    @Binds
    abstract fun bindcontext(context: Application): Context

    @Binds
    abstract fun bindsFavoriteRepositoryUseCase(favoriteRepositoryImpl: FavoriteRepositoryImpl): FavoriteRepositoryUseCase

    @Binds
    abstract fun bindsDataRepositoryUseCase(networkDataRepositoryImpl: NetworkDataRepositoryImpl): DataRepositoryUseCase

    @Binds
    abstract fun bindsUserRepository(networkDataRepositoryImpl: UserProfileProfileRepository): UserProfileRepositoryDataSource

}