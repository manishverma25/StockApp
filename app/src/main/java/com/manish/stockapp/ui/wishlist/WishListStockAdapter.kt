package com.manish.stockapp.ui.wishlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manish.stockapp.R
import com.manish.stockapp.data.FavoriteStockDetails
import kotlinx.android.synthetic.main.layout_stock_details_item.view.*


class WishListStockAdapter : RecyclerView.Adapter<WishListStockAdapter.StockDetailsViewHolder>() {

    inner class StockDetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<FavoriteStockDetails>() {
        override fun areItemsTheSame(oldItem: FavoriteStockDetails, newItem: FavoriteStockDetails): Boolean {
            return oldItem.sid == newItem.sid
        }

        override fun areContentsTheSame(oldItem: FavoriteStockDetails, newItem: FavoriteStockDetails): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StockDetailsViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.layout_wishlist_stock_item,
            parent,
            false
        )
    )
    override fun getItemCount() =  differ.currentList.size

    override fun onBindViewHolder(holder: StockDetailsViewHolder, position: Int) {
        val stockDetailItem = differ.currentList[position]
        holder.itemView.apply {
            stockNameTxt.text = stockDetailItem.sid
        }


    }
}