package com.manish.stockapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.manish.stockapp.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadi.retrofitmvvm.util.Utils
import com.manish.stockapp.StockApplication
import com.manish.stockapp.ViewModelFactory
import com.manish.stockapp.domain.FavoriteRepositoryImpl
import com.manish.stockapp.data.Resource
import com.manish.stockapp.ViewModelProviderFactory
import com.manish.stockapp.domain.NetworkDataRepositoryImpl
import kotlinx.android.synthetic.main.fragment_home_layout.*
import javax.inject.Inject


class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }


    lateinit var stockDetailsAdapter: StockDetailsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_layout, container, false)
    }

    private fun injectDI() {

        StockApplication.appComponent.inject(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDI()
        init()

    }


    private fun init() {
        rvPics.setHasFixedSize(true)
        rvPics.layoutManager = LinearLayoutManager(activity)
        stockDetailsAdapter = StockDetailsAdapter()
        setupViewModel()
    }


    private fun setupViewModel() {
        val favoriteRepository = FavoriteRepositoryImpl()
        val networkRepository = NetworkDataRepositoryImpl()
//        val factory =
//            ViewModelProviderFactory(activity?.applicationContext as StockApplication,networkRepository, favoriteRepository)
//        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        observerLiveData()
        viewModel.getStocksData()
    }


    private fun observerLiveData() {
        Log.d(TAG, " called observerLiveData() .2222. ")
        observerStockDetailLiveData()
        observeResetSelectedItemListLiveData()

    }

    private fun observerStockDetailLiveData(){
        viewModel.stockDetailLiveData.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { stockDetailsResponse ->
                        Log.d(TAG, " stockDetailsResponsessage data  :  ${stockDetailsResponse.data}")
                        stockDetailsAdapter.differ.submitList(stockDetailsResponse.data)
                        rvPics.adapter = stockDetailsAdapter
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.d(TAG, " message $message")
//                        rootLayout.errorSnack(message,Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun observeResetSelectedItemListLiveData(){
        viewModel.isNeedToResetSelectedItemListLiveData.observe(requireActivity(), Observer { isNeedToReset ->

            if(isNeedToReset){

                Utils.resetSelectedStockList(stockDetailsAdapter.differ.currentList)
                stockDetailsAdapter.notifyDataSetChanged() //todo optimize it
                viewModel.setIsNeedTpResetSelectedItemListLiveData(false)
            }



        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> {
                doFavorite()
            }

            R.id.menu_delete_favorite -> {
                viewModel.doAllUnFavorite()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun doFavorite(){
        viewModel.doSaveFavorite()
    }

    private fun hideProgressBar() {
        progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        progress.visibility = View.VISIBLE
    }



    override fun onStart() {
        super.onStart()


    }




    override fun onStop() {
        super.onStop()
    }




    companion object {
        fun newInstance(): HomeFragment {
            return   HomeFragment()
        }
        val TAG = "DashboardFragment"
    }




}
