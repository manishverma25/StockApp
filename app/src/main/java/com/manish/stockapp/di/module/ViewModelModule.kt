package com.manish.stockapp.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manish.stockapp.ViewModelFactory
import com.manish.stockapp.ui.home.HomeViewModel
import com.manish.stockapp.ui.profile.ProfileViewModel
import com.manish.stockapp.ui.wishlist.WishListViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)



@Module
abstract class ViewModelModule {
/*

    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
*/


    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(WishListViewModel::class)
    abstract fun bindWishListViewModel(viewModel: WishListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

}