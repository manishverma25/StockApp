package com.manish.stockapp.di.component

import android.app.Application
import com.manish.stockapp.StockApplication
import com.manish.stockapp.ViewModelFactory
import com.manish.stockapp.di.module.AppModule
import com.manish.stockapp.di.module.NetworkModule
import com.manish.stockapp.di.module.ViewModelModule
import com.manish.stockapp.ui.base.MainActivity
import com.manish.stockapp.ui.home.HomeFragment
import com.manish.stockapp.ui.profile.ProfileFragment
import com.manish.stockapp.ui.wishlist.WishListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [NetworkModule::class,
        ViewModelModule::class,
        AppModule::class,
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }


    fun viewModelsFactory(): ViewModelFactory

    fun inject(stockApplication: StockApplication)

    fun inject(mainActivity: MainActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(mainActivity: WishListFragment)
    fun inject(mainActivity: ProfileFragment)

}