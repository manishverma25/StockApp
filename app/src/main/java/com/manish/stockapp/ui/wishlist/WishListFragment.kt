package com.manish.stockapp.ui.wishlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.manish.stockapp.R

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.manish.stockapp.StockApplication
import com.manish.stockapp.ViewModelFactory
import com.manish.stockapp.util.extension.errorSnack
import kotlinx.android.synthetic.main.wishlist_fragment.*
import javax.inject.Inject


class WishListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val wishListViewModel: WishListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(WishListViewModel::class.java)
    }

    lateinit var wishListStockAdapter: WishListRecyclerAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wishlist_fragment, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDI()
        init();
    }

    private fun init() {
        wishListRecyclerView.setHasFixedSize(true)
        wishListRecyclerView.layoutManager = LinearLayoutManager(activity)
        wishListStockAdapter = WishListRecyclerAdapter()

        observerLiveData()
        getFavoriteStockList()

    }

    private fun injectDI() {
        StockApplication.appComponent.inject(this)
    }


    private fun getFavoriteStockList() {
        wishListViewModel.getFavoriteStockListLiveData()

    }

    private fun observerLiveData() {
        observerWishListViewModelStateLiveData()
    }

    private fun observerWishListViewModelStateLiveData() {

        wishListViewModel.wishListViewModelStateLiveData.observe(
            requireActivity(),
            wishListViewModelStateObserver)

    }
    val wishListViewModelStateObserver = Observer<WishListViewModelState>(){
                wishListViewModelState ->

            Log.d(TAG, " wishListViewModelState  ... $wishListViewModelState ")
            when (wishListViewModelState) {
                WishListViewModelState.Loading -> {
                    showProgressBar()
                }
                wishListViewModelState as WishListViewModelState.Success -> {
                    hideProgressBar()
                    Log.d(TAG, " wishListViewModelState.data   ... ${wishListViewModelState.data} ")
                    wishListStockAdapter.differ.submitList(wishListViewModelState.data)
                    wishListRecyclerView.adapter = wishListStockAdapter
                }
                wishListViewModelState as WishListViewModelState.Error -> {
                    hideProgressBar()
                    progress.errorSnack(
                        wishListViewModelState.errorMessage,
                        Snackbar.LENGTH_LONG
                    )
                }

                else -> {

                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG," onDestroyView")
        wishListViewModel.wishListViewModelStateLiveData.removeObserver(wishListViewModelStateObserver)

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG," onDestroy")
    }


    private fun hideProgressBar() {
        progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        progress.visibility = View.VISIBLE
    }



    companion object {
        val TAG = "WishListFragment"
        fun newInstance(): WishListFragment {
            return WishListFragment()
        }

    }

}
