package com.manish.stockapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.manish.stockapp.TestCoroutineContextProvider
import com.manish.stockapp.TestCoroutineRule
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsApiResponse
import com.manish.stockapp.data.StockDetailsItem
import com.manish.stockapp.data.repository.NetworkRepositoryDataSource
import com.manish.stockapp.data.repository.FavoriteRepositoryDataSource
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
class HomeViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    lateinit var homeViewModel: HomeViewModel

    @Mock
    lateinit var stateObserver: Observer<Resource<StockDetailsApiResponse>>

    @Mock
    lateinit var favoriteStatusObserver: Observer<Resource<String>>

    @Mock lateinit var networkNetworkRepositoryDataSourceImpl: NetworkRepositoryDataSource
    @Mock lateinit var favoriteRepositoryImpl: FavoriteRepositoryDataSource

    val apiErrorMsg =  "Internal server error 500"

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



    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        homeViewModel = HomeViewModel(networkNetworkRepositoryDataSourceImpl,favoriteRepositoryImpl, TestCoroutineContextProvider())
        homeViewModel.stocksDetailApiStatusLiveData.observeForever(stateObserver)
        homeViewModel.favoriteStatusLiveData.observeForever(favoriteStatusObserver)
    }

    @Test
    fun `fetchStockDetails api Data success `() {

        val mockedStockDetailsResponse  = StockDetailsApiResponse(true,  arrayListOf(  mockedStockDetailsItem ))
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(networkNetworkRepositoryDataSourceImpl.getStocksDetails()).thenReturn(
                Resource.Success(mockedStockDetailsResponse)
            )
            homeViewModel.fetchStockDetailsData()

            val ac = ArgumentCaptor.forClass(Resource::class.java)
            Mockito.verify(stateObserver, Mockito.times(2)).onChanged(ac.capture() as Resource<StockDetailsApiResponse>?)

            Assert.assertThat(ac.allValues[0], CoreMatchers.instanceOf(Resource.Loading::class.java))
            Assert.assertThat(ac.allValues[1], CoreMatchers.instanceOf(Resource.Success::class.java))
            Assert.assertThat(ac.allValues[1].data, CoreMatchers.equalTo(mockedStockDetailsResponse))
        }
    }


    @Test
    fun `fetchStockDetails api fail`() {


        testCoroutineRule.runBlockingTest {
            Mockito.`when`(networkNetworkRepositoryDataSourceImpl.getStocksDetails()).thenReturn(
                Resource.Error(apiErrorMsg)
            )
            homeViewModel.fetchStockDetailsData()

            val ac = ArgumentCaptor.forClass(Resource::class.java)
            Mockito.verify(stateObserver, Mockito.times(2))
                .onChanged(ac.capture() as Resource<StockDetailsApiResponse>?)

            Assert.assertThat(ac.allValues[0], CoreMatchers.instanceOf(Resource.Loading::class.java))
            Assert.assertThat(ac.allValues[1], CoreMatchers.instanceOf(Resource.Error::class.java))
            Assert.assertThat(ac.allValues[1].message, CoreMatchers.equalTo(apiErrorMsg))
        }
    }


    @Test
    fun `do favorite stock successfully done ` () {

        val mockedFavoriteResponse  = Resource.Success("")
        testCoroutineRule.runBlockingTest {
            Mockito.`when`( favoriteRepositoryImpl.doFavorite(mockedStockDetailsItem)).thenReturn(
                Resource.Success("")
            )
            homeViewModel.doFavorite(mockedStockDetailsItem)

            val ac = ArgumentCaptor.forClass(Resource::class.java)
            Mockito.verify(favoriteStatusObserver, Mockito.times(2))
                .onChanged(ac.capture() as Resource<String>?)

            Assert.assertThat(ac.allValues[0], CoreMatchers.instanceOf(Resource.Loading::class.java))
            Assert.assertThat(ac.allValues[1], CoreMatchers.instanceOf(Resource.Success::class.java))
            Assert.assertThat(ac.allValues[1].data, CoreMatchers.equalTo(mockedFavoriteResponse))
        }
    }



    @Test
    fun `do favorite stock get failed ` () {

        testCoroutineRule.runBlockingTest {
            Mockito.`when`( favoriteRepositoryImpl.doFavorite(mockedStockDetailsItem)).thenReturn(
                Resource.Error(  apiErrorMsg,String())
            )
            homeViewModel.doFavorite(mockedStockDetailsItem)

            val ac = ArgumentCaptor.forClass(Resource::class.java)
            Mockito.verify(favoriteStatusObserver, Mockito.times(2))
                .onChanged(ac.capture() as Resource<String>?)

            Assert.assertThat(ac.allValues[0], CoreMatchers.instanceOf(Resource.Loading::class.java))
            Assert.assertThat(ac.allValues[1], CoreMatchers.instanceOf(Resource.Error::class.java))
            Assert.assertThat(ac.allValues[1].data, CoreMatchers.equalTo(apiErrorMsg))
        }
    }
}