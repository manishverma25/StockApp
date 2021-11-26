package com.manish.stockapp.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
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
import com.manish.stockapp.util.Constants
import kotlinx.android.synthetic.main.fragment_home_layout.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }


    val perodicHandler = Handler(Looper.getMainLooper())

    val mRunnable = object: Runnable {
        override fun run() {
            viewModel.getStocksData()
            perodicHandler.postDelayed(this, Constants.STOCK_DEATILS_API_PERIODIC_TIMER)
        }
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

    override fun onStart() {
        super.onStart()
        /**
         * Start the api call periodically after 5 sec
         */
       startPerodicAPiCall()
    }

    override fun onStop() {
        super.onStop()

        /**
         * Stop  the api call periodically after 5 sec
         */
        stopPerodicApiCall()
    }
//    val executorService =   Executors.newSingleThreadScheduledExecutor()

    fun startPerodicAPiCall(){
      perodicHandler.post(mRunnable)

//        executorService.scheduleAtFixedRate({
//            Log.d(TAG, " executorService called  ..  ")
//            viewModel.getStocksData()
//
//        }, 1, 2, TimeUnit.SECONDS)
    }


    fun stopPerodicApiCall(){
        perodicHandler.removeCallbacks(mRunnable)
    }



    private fun init() {
        rvPics.setHasFixedSize(true)
        rvPics.layoutManager = LinearLayoutManager(activity)
        stockDetailsAdapter = StockDetailsAdapter()
        setupViewModel()
    }


    private fun setupViewModel() {
        observerLiveData()

    }


    private fun observerLiveData() {
        Log.d(TAG, " called observerLiveData() .2222. ")
        observerStockDetailLiveData()
        observeResetSelectedItemListLiveData()
        observeStockDetailsApiHitLiveData()

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
                stockDetailsAdapter.notifyDataSetChanged()
                viewModel.setIsNeedTpResetSelectedItemListLiveData(false)
            }
        })
    }



    private fun observeStockDetailsApiHitLiveData(){
        viewModel.stockDetailsApiHitLiveData.observe(requireActivity(), Observer { isApiHit ->

            Log.d(TAG, " observeStockDetailsApiHitLiveData ... $isApiHit   . ")
            if(isApiHit){
                Toast.makeText(requireContext(),"Stock live details API Hit ",Toast.LENGTH_SHORT).show()
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








    companion object {
        fun newInstance(): HomeFragment {
            return   HomeFragment()
        }
        val TAG = "HomeFragment"
    }




}
