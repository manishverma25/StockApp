package com.manish.stockapp.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import com.manish.stockapp.R
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.manish.stockapp.StockApplication
import com.manish.stockapp.ViewModelFactory
import com.manish.stockapp.data.Resource
import com.manish.stockapp.data.StockDetailsApiResponse
import com.manish.stockapp.data.StockDetailsItem
import com.manish.stockapp.ui.base.BaseFragment
import com.manish.stockapp.util.Constants
import com.manish.stockapp.util.extension.errorSnack
import com.manish.stockapp.util.extension.showSnack
import com.manish.stockapp.util.extension.toGone
import com.manish.stockapp.util.extension.toVisible
import kotlinx.android.synthetic.main.home_fragment.*
import javax.inject.Inject


class HomeFragment : BaseFragment(), StockDetailsAdapter.OnStockItemSelectListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }


    val periodicApiPollingHandler = Handler(Looper.getMainLooper())

    var periodicApiPollingRunnableTask :Runnable? = object: Runnable {
        override fun run() {
            viewModel.fetchStockDetailsData()
            periodicApiPollingHandler.postDelayed(this, Constants.STOCK_DEATILS_API_PERIODIC_TIMER)
        }
    }


     var stockDetailsListAdapter: StockDetailsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
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
         * Start the api call periodically
         */
       startPerodicAPiCall()  //stop perodic call having bug
    }

    override fun onStop() {
        super.onStop()

        /**
         * Stop  the api call periodically
         */
        stopPerodicApiCall()
    }

    fun startPerodicAPiCall(){
        periodicApiPollingRunnableTask?.let {
            periodicApiPollingHandler.post(it)
        }
    }


    fun stopPerodicApiCall(){
        periodicApiPollingRunnableTask?.let {
            periodicApiPollingHandler.removeCallbacks(it)
        }

    }



    private fun init() {
        stockDeatilsRecyclerView.setHasFixedSize(true)
        stockDeatilsRecyclerView.layoutManager = LinearLayoutManager(activity)
        stockDetailsListAdapter = StockDetailsAdapter(this)
        setupViewModel()

    }

    override fun onStockItemSelectListener(stockDetailItem: StockDetailsItem) {
        viewModel.doFavorite(stockDetailItem)
    }


    private fun setupViewModel() {
        observerLiveData()

    }


    private fun observerLiveData() {
        viewModel.favoriteStatusLiveData.observe(viewLifecycleOwner, ::handleFavoriteResponse)
        viewModel.stockDetailsApiTrackingLiveData.observe(viewLifecycleOwner, ::handleStockDetailsApiTracking)
        viewModel.stocksDetailApiStatusLiveData.observe(viewLifecycleOwner, ::handleStockDetailsApi)

    }


    private fun handleStockDetailsApiTracking(isApiHit: Boolean) {
        if(isApiHit){
            Toast.makeText(requireContext(),getString(R.string.stocks_live_tracking_api_hit),Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleFavoriteResponse(response: Resource<String>) {
        when (response) {
            is Resource.Success -> {
                progress.toGone()
                progress.showSnack(
                    getString(R.string.stock_favorite_success_msg),
                    Snackbar.LENGTH_LONG
                )
            }
            is Resource.Error -> {
                progress.toGone()
                response.message?.let { message ->
                    progress.errorSnack(
                        getString(R.string.stock_favorite_error_msg),
                        Snackbar.LENGTH_LONG
                    )
                }
            }

            is Resource.Loading -> {
                progress.toVisible()
            }
        }
    }



    private fun handleStockDetailsApi(response :Resource<StockDetailsApiResponse>){
        when (response) {
            is Resource.Success -> {
                progress.toGone()
                response.data?.let { stockDetailsResponse ->
                    Log.d(TAG, " stockDetailsResponsessage data  :  ${stockDetailsResponse.data}")
                    stockDetailsListAdapter?.differ?.submitList(stockDetailsResponse.data)
                    stockDeatilsRecyclerView.adapter = stockDetailsListAdapter
                }
            }
            is Resource.Error -> {
                progress.toGone()
                response.message?.let { message ->
                    Log.d(TAG, " message $message")
                    progress.errorSnack(message, Snackbar.LENGTH_LONG)
                }
            }

            is Resource.Loading -> {
                progress.toVisible()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        periodicApiPollingRunnableTask = null
        stockDetailsListAdapter?.onStockItemSelectListener = null
        stockDetailsListAdapter = null
    }

    companion object {
        fun newInstance(): HomeFragment {
            return   HomeFragment()
        }
        val TAG = "HomeFragment"
    }

}


