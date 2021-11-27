package com.manish.stockapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.manish.stockapp.TestCoroutineContextProvider
import com.manish.stockapp.TestCoroutineRule
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsApiResponse
import com.manish.stockapp.data.StockDetailsItem
import com.manish.stockapp.domain.DataRepositoryUseCase
import com.manish.stockapp.domain.FavoriteRepositoryUseCase
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

    @Mock lateinit var networkDataRepositoryUseCaseImpl: DataRepositoryUseCase
    @Mock lateinit var favoriteRepositoryImpl: FavoriteRepositoryUseCase

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
//        appContext
//        val f = FirebaseFirestore.getInstance()
//        Log.d("mvv","FirebaseFirestore  .... "+appContext)
        homeViewModel = HomeViewModel(networkDataRepositoryUseCaseImpl,favoriteRepositoryImpl, TestCoroutineContextProvider())
        homeViewModel.stockDetailLiveData.observeForever(stateObserver)
    }

    // error returnded from use case, error state shud be retrurned
    // empty data returned, error state shud be returned




    // success data from usecase, success state shud be triggered
    @Test
    fun getData_successFromUseCase_loadingAndSuccessReturned() {

        val mockedStockDetailsResponse  = StockDetailsApiResponse(true,  arrayListOf(  mockedStockDetailsItem ))
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(networkDataRepositoryUseCaseImpl.getStocksDetails()).thenReturn(
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
    fun getData_api_errorFromUseCase_loadingAndSuccessReturned() {

       val apiErrorMsg =  "Internal server error 500"
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(networkDataRepositoryUseCaseImpl.getStocksDetails()).thenReturn(
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
}