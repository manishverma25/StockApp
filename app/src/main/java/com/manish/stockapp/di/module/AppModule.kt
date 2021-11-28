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
    abstract fun bindsFavoriteRepositoryUseCase(favoriteRepository: FavoriteRepository): FavoriteRepositoryDataSource

    @Binds
    abstract fun bindsDataRepositoryUseCase(networkDataRepositoryImpl: NetworkNetworkRepositoryImpl): NetworkRepositoryDataSource

    @Binds
    abstract fun bindsUserRepository(networkDataRepositoryImpl: UserProfileProfileRepository): UserProfileRepositoryDataSource

}