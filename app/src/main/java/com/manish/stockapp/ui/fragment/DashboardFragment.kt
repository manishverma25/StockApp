package com.manish.stockapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.manish.stockapp.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.manish.stockapp.app.StockApplication
import com.manish.stockapp.repository.StockDetailsRepository
import com.manish.stockapp.ui.adapter.StockDetailsAdapter
import com.manish.stockapp.util.Resource
import com.manish.stockapp.viewmodel.DashboardViewModel
import com.manish.stockapp.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_home_layout.*


class DashboardFragment : Fragment() {
    private lateinit var viewModel: DashboardViewModel
    lateinit var stockDetailsAdapter: StockDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    private fun init() {
        rvPics.setHasFixedSize(true)
        rvPics.layoutManager = LinearLayoutManager(activity)
        stockDetailsAdapter = StockDetailsAdapter()
        setupViewModel()
    }


    private fun setupViewModel() {
        val repository = StockDetailsRepository()
        val factory =
            ViewModelProviderFactory(activity?.applicationContext as StockApplication, repository)
        viewModel = ViewModelProvider(this, factory).get(DashboardViewModel::class.java)
        getStocksDetails()
    }


    private fun getStocksDetails() {
        Log.d(TAG, " called getStocksDetails() .. ")
        viewModel.stockDetailLiveData.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { stockDetailsResponse ->
                        Log.d(TAG, " stockDetailsResponsessage :  ${stockDetailsResponse}")
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

    private fun hideProgressBar() {
        progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        progress.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(param1: String?, param2: String?): DashboardFragment {
            val fragment = DashboardFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        val TAG = "DashboardFragment"
    }




}
