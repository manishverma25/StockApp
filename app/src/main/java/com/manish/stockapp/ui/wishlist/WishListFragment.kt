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

    lateinit var wishListStockAdapter: WishListRecyclerAdapter

    private var adapter: WishListAdapter? = null

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
        observerFavoriteStockListLiveData()
        observerWishListErrorLiveData()
    }

    private fun observerFavoriteStockListLiveData() {

        wishListViewModel.favoriteStockListLiveData.observe(requireActivity(),  { favoriteStockList ->

            hideProgressBar()
            Log.d(TAG, " favoriteStockList  ... $favoriteStockList ")
            wishListStockAdapter.differ.submitList (favoriteStockList)
            wishListRecyclerView.adapter = wishListStockAdapter
        })

    }

    private fun observerWishListErrorLiveData() {

        wishListViewModel.wishListSnapShotListenerErrorLiveData.observe(requireActivity(),  { wishListSnapSnopListenerError ->
            hideProgressBar()
            progress.errorSnack(wishListSnapSnopListenerError, Snackbar.LENGTH_LONG)
        })

    }

    private fun hideProgressBar() {
        progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        progress.visibility = View.VISIBLE
    }


    override fun onStart() {
        super.onStart()
        adapter?.startListening();
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening();
    }


    companion object {
        val TAG = "WishListFragment"
        fun newInstance(): WishListFragment {
            return WishListFragment()
        }

    }

}
