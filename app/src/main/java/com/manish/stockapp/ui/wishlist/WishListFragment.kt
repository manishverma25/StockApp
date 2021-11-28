package com.manish.stockapp.ui.wishlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    lateinit var wishListStockAdapter: WishListStockAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wishlist_fragment, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDI()
        setUpUI();

        /***
         *
         * called it from onviewCreated as it get called when this fragment changed
         *
         *  If it would have called from init {} block of viewmodel ,it won't called in that case
         *  but to get updated watchlist data every time  on slecting  wishlist fragment, it called from onViewCreated
         *
         */
        getFavoriteStockList()
    }

    private fun getFavoriteStockList() {
        wishListViewModel.fetchFavoriteStocksList()

    }

    private fun observerLiveData() {
        wishListViewModel.wishListStocksListLiveData.observe(viewLifecycleOwner, ::handleWishListStocksListResponse)
    }

    private fun setUpUI() {
        wishListRecyclerView.setHasFixedSize(true)
        wishListRecyclerView.layoutManager = LinearLayoutManager(activity)
        wishListStockAdapter = WishListStockAdapter()

        observerLiveData()

    }

    private fun injectDI() {
        StockApplication.appComponent.inject(this)
    }



    private fun handleWishListStocksListResponse(wishListStocksListResponse: WishListViewModelState?) {
        Log.d(TAG, " wishListViewModelState  ... $wishListStocksListResponse ")
        when (wishListStocksListResponse) {
            WishListViewModelState.Loading -> {
                showProgressBar()
            }
            wishListStocksListResponse as WishListViewModelState.Success -> {
                hideProgressBar()
                Log.d(TAG, " wishListViewModelState.data   ... ${wishListStocksListResponse.data} ")
                wishListStockAdapter.differ.submitList(wishListStocksListResponse.data)
                wishListRecyclerView.adapter = wishListStockAdapter
            }
            wishListStocksListResponse as WishListViewModelState.Error -> {
                hideProgressBar()
                progress.errorSnack(
                    wishListStocksListResponse.errorMessage,
                    Snackbar.LENGTH_LONG
                )
            }
        }
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
