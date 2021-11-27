package com.manish.stockapp.util.extension

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.IntegerRes
import com.google.android.material.snackbar.Snackbar




fun View.showSnack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.show()
}


fun View.errorSnack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.setActionTextColor(Color.parseColor("#FFFFFF"))
    snack.view.setBackgroundColor(Color.parseColor("#C62828"))
    snack.show()
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

fun Context.toast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun ProgressBar.show(){
    this.visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    this.visibility = View.GONE
}
