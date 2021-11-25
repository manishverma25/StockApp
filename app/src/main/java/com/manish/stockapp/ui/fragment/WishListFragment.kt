package com.manish.stockapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.manish.stockapp.R

import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.LinearLayoutManager



import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.manish.stockapp.model.StockDetailsModel
import com.manish.stockapp.ui.adapter.WishListAdapter
import com.manish.stockapp.util.Constants.FIREBASE_COLLECTION_PATH
import kotlinx.android.synthetic.main.fragment_second.*


class WishListFragment : Fragment() {


    private val db = FirebaseFirestore.getInstance()
    private val notebookRef = db.collection(FIREBASE_COLLECTION_PATH)

    private var adapter: WishListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView();
    }

    private fun setUpRecyclerView() {
        val query: Query = notebookRef.whereEqualTo("favorite", false)
        val options: FirestoreRecyclerOptions<StockDetailsModel> = FirestoreRecyclerOptions.Builder<StockDetailsModel>()
            .setQuery(query, StockDetailsModel::class.java)
            .build()
        adapter = WishListAdapter(options)
        wishListRecyclerView.setHasFixedSize(true)
        wishListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        wishListRecyclerView.adapter = adapter
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
        fun newInstance( param1: String?, param2: String?): WishListFragment {
            val fragment = WishListFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
    }

}
