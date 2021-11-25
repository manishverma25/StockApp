package com.manish.stockapp.ui.adapter


import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater

import android.view.ViewGroup

import com.manish.stockapp.model.StockDetailsModel

import android.view.View;
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.manish.stockapp.R


class WishListAdapter(options: FirestoreRecyclerOptions<StockDetailsModel>) :
    FirestoreRecyclerAdapter<StockDetailsModel, WishListAdapter.NoteHolder?>(options) {
     override fun onBindViewHolder(holder: NoteHolder, position: Int, model: StockDetailsModel) {
         holder.sidTxt.text = model.sid
        holder.priceTxt.text = model.price.toString()
         holder.changePriceTxt.text = model.change.toString()
         holder.stockChangePriceImage   //todo 11 add logic for up down

         holder.stockItemCheckBox.visibility = View.GONE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_stock_details_item,
            parent, false
        )
        return NoteHolder(v)
    }

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sidTxt: AppCompatTextView = itemView.findViewById(R.id.sidTxt)
        var priceTxt: AppCompatTextView = itemView.findViewById(R.id.priceTxt)
        var changePriceTxt: AppCompatTextView = itemView.findViewById(R.id.changePriceTxt)
        var stockChangePriceImage: AppCompatImageView = itemView.findViewById(R.id.stockChangePriceImage)
        var stockItemCheckBox: AppCompatCheckBox = itemView.findViewById(R.id.stockItemCheckBox)

    }
}