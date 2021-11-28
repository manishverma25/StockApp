package com.manish.stockapp.ui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.manish.stockapp.TestCoroutineContextProvider
import com.manish.stockapp.TestCoroutineRule
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.repository.UserProfileRepositoryDataSource
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


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        profileViewModel = ProfileViewModel(userProfileRepository, TestCoroutineContextProvider())
        profileViewModel.userNameLiveData.observeForever(userNameLiveDataObserver)
        profileViewModel.signOutSuccessStatusLivaData.observeForever(signOutSuccessStatusObserver)
    }

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
                Resource.Error("")
            )
            profileViewModel.fetchUserName()

            val ac = ArgumentCaptor.forClass(Resource::class.java)
            Mockito.verify(userNameLiveDataObserver, Mockito.times(1))
                .onChanged(ac.capture() as Resource<String>?  )

            Assert.assertThat(ac.allValues[0], CoreMatchers.instanceOf( Resource.Error::class.java))
        }
    }

//
}