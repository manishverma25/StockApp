package com.manish.stockapp.model

data class StockDetailsModel(val sid:String,
                             val price:Float,
                             val close:Float,
                             val change:Float,
                             val high:Float,
                             val low:Float,
                             val volume:Long,
                             val date:String,
                             var isFavorite :Boolean = false,
                             var isSelected :Boolean = false
)
