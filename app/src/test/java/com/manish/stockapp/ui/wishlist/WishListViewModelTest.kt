package com.manish.stockapp.ui.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.manish.stockapp.TestCoroutineContextProvider
import com.manish.stockapp.TestCoroutineRule
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsItem
import com.manish.stockapp.domain.FavoriteRepositoryDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.junit.Assert
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class WishListViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    lateinit var wishListViewModel: WishListViewModel

    @Mock
    lateinit var WishListViewModelStateObserver: Observer<WishListViewModelState>

    @Mock lateinit var favoriteRepositoryImpl: FavoriteRepositoryDataSource

    val mockedStockDetailsItem = StockDetailsItem(
        sid = "RELI",
        price = 2485.25F,
        close = 12F,
        change= 133.85F,
        high =  2496F,
        low = 2357.15F,
        volume = 16425378,
        date = "2021-11-25T08:48:12.000Z"

    )
    val apiErrorMsg =  "Internal server error 500"

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        wishListViewModel = WishListViewModel(favoriteRepositoryImpl, TestCoroutineContextProvider())
        wishListViewModel.wishListStocksListLiveData.observeForever(WishListViewModelStateObserver)
    }

    @Test
    fun  `get favorite stock list success`() {

        val mockedFavvoriteStockList  =  arrayListOf(  mockedStockDetailsItem )
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(favoriteRepositoryImpl.getFavoriteStocksCollection()).thenReturn(
                Resource.Success(mockedFavvoriteStockList)
            )
            wishListViewModel.fetchFavoriteStocksList()

            val ac = ArgumentCaptor.forClass(Resource::class.java)
            Mockito.verify(WishListViewModelStateObserver, Mockito.times(2))
                .onChanged(ac.capture() as WishListViewModelState?)

            Assert.assertThat(ac.allValues[0], CoreMatchers.instanceOf( WishListViewModelState.Loading::class.java))
            Assert.assertThat(ac.allValues[1], CoreMatchers.instanceOf( WishListViewModelState.Success::class.java))
        }
    }


    @Test
    fun `get favorite stock list error`() {

        testCoroutineRule.runBlockingTest {
            Mockito.`when`(favoriteRepositoryImpl.getFavoriteStocksCollection()).thenReturn(
                Resource.Error(apiErrorMsg)
            )
            wishListViewModel.fetchFavoriteStocksList()

            val ac = ArgumentCaptor.forClass(Resource::class.java)
            Mockito.verify(WishListViewModelStateObserver, Mockito.times(2))
                .onChanged(ac.capture() as WishListViewModelState?)

            Assert.assertThat(
                ac.allValues[0],
                CoreMatchers.instanceOf(WishListViewModelState.Loading::class.java)
            )
            Assert.assertThat(ac.allValues[1], CoreMatchers.instanceOf(WishListViewModelState.Error::class.java))
        }
    }
}