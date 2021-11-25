package com.manish.stockapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.manish.stockapp.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadi.retrofitmvvm.util.Utils
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
        observerLiveData()
    }


    private fun observerLiveData() {
        Log.d(TAG, " called observerLiveData() .. ")
        observerStockDetailLiveData()
        observeResetSelectedItemListLiveData()

    }

    private fun observerStockDetailLiveData(){
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

    private fun observeResetSelectedItemListLiveData(){
        viewModel.isNeedTpResetSelectedItemListLiveData.observe(requireActivity(), Observer { isNeedToReset ->

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
                viewModel.doSaveFavorite()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
