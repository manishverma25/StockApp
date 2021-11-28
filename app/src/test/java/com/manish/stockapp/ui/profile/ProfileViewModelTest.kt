package com.manish.stockapp.ui.profile

import org.junit.Assert.*

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.manish.stockapp.TestCoroutineContextProvider
import com.manish.stockapp.TestCoroutineRule
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsItem
import com.manish.stockapp.domain.UserProfileRepositoryDataSource
import com.manish.stockapp.util.Constants
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
class ProfileViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    lateinit var profileViewModel: ProfileViewModel

    @Mock
    lateinit var userNameLiveDataObserver: Observer<Resource<String>>


    @Mock
    lateinit var signOutSuccessStatusObserver: Observer<Resource<String>>

    @Mock lateinit var userProfileRepository: UserProfileRepositoryDataSource


    val mockedUseName = "Test"

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
        profileViewModel = ProfileViewModel(userProfileRepository, TestCoroutineContextProvider())
        profileViewModel.userNameLiveData.observeForever(userNameLiveDataObserver)
        profileViewModel.signOutSuccessStatusLivaData.observeForever(signOutSuccessStatusObserver)
    }

    // success data from usecase, success state shud be triggered
    @Test
    fun `get user name sccucess`() {

        testCoroutineRule.runBlockingTest {
            Mockito.`when`(userProfileRepository.getUserName()).thenReturn(
                Resource.Success(mockedUseName)
            )
            profileViewModel.fetchUserName()

            val ac = ArgumentCaptor.forClass(Resource::class.java)
            Mockito.verify(userNameLiveDataObserver, Mockito.times(1))
                .onChanged(ac.capture() as Resource<String>?  )

            Assert.assertThat(ac.allValues[0], CoreMatchers.instanceOf( Resource.Success::class.java))
        }
    }


    @Test
    fun `get user name error`() {

        testCoroutineRule.runBlockingTest {
            Mockito.`when`(userProfileRepository.getUserName()).thenReturn(
                Resource.Error(apiErrorMsg)
            )
            profileViewModel.fetchUserName()

            val ac = ArgumentCaptor.forClass(Resource::class.java)
            Mockito.verify(userNameLiveDataObserver, Mockito.times(1))
                .onChanged(ac.capture() as Resource<String>?  )

            Assert.assertThat(ac.allValues[0], CoreMatchers.instanceOf( Resource.Error::class.java))
            Assert.assertThat(ac.allValues[0].message, CoreMatchers.equalTo(apiErrorMsg))
        }
    }

//
}