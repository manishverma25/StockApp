package com.manish.stockapp.ui.wishlist


import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater

import android.view.ViewGroup

import com.manish.stockapp.data.StockDetailsItem

import android.view.View;
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.manish.stockapp.R


class WishListAdapter(options: FirestoreRecyclerOptions<StockDetailsItem>) :
    FirestoreRecyclerAdapter<StockDetailsItem, WishListAdapter.NoteHolder?>(options) {
     override fun onBindViewHolder(holder: NoteHolder, position: Int, item: StockDetailsItem) {
         holder.sidTxt.text = item.sid
        holder.priceTxt.text = item.price.toString()
         holder.changePriceTxt.text = item.change.toString()
         holder.stockChangePriceImage   //todo 11 add logic for up down
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_wishlist_stock_item,
            parent, false
        )
        return NoteHolder(v)
    }

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sidTxt: AppCompatTextView = itemView.findViewById(R.id.stockNameTxt)
        var priceTxt: AppCompatTextView = itemView.findViewById(R.id.priceTxt)
        var changePriceTxt: AppCompatTextView = itemView.findViewById(R.id.changePriceTxt)
        var stockChangePriceImage: AppCompatImageView = itemView.findViewById(R.id.stockChangePriceImage)

    }
}