package com.manish.stockapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manish.stockapp.R
import com.manish.stockapp.data.StockDetailsItem
import kotlinx.android.synthetic.main.layout_stock_details_item.view.*


class StockDetailsAdapter (var onStockItemSelectListener :OnStockItemSelectListener?) : RecyclerView.Adapter<StockDetailsAdapter.StockDetailsViewHolder>() {

    inner class StockDetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<StockDetailsItem>() {
        override fun areItemsTheSame(oldItem: StockDetailsItem, newItem: StockDetailsItem): Boolean {
            return oldItem.sid == newItem.sid
        }

        override fun areContentsTheSame(oldItem: StockDetailsItem, newItem: StockDetailsItem): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StockDetailsViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.layout_stock_details_item,
            parent,
            false
        )
    )
    override fun getItemCount() =  differ.currentList.size

    override fun onBindViewHolder(holder: StockDetailsViewHolder, position: Int) {
        val stockDetailItem = differ.currentList[position]
        holder.itemView.apply {
            stockNameTxt.text = stockDetailItem.sid
            changePriceTxt.text = stockDetailItem.change.toString()
            priceTxt.text = stockDetailItem.price.toString()
            if(stockDetailItem.change >0){
                stockChangePriceImage.setImageResource(R.drawable.ic_up_icon)
            }else{
                stockChangePriceImage.setImageResource(R.drawable.ic_down_icon)
            }
        }

        holder.itemView.stockItemCheckBox.visibility = View.VISIBLE
        holder.itemView.stockItemCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onStockItemSelectListener?.onStockItemSelectListener(stockDetailItem)
        }
    }

     interface OnStockItemSelectListener {

        fun onStockItemSelectListener(stockDetailItem :StockDetailsItem)
    }


}