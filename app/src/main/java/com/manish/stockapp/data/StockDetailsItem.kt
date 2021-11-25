package com.manish.stockapp.data



data class StockDetailsItem(var sid:String = "",
                            var price:Float = 0F,
                            var close:Float  = 0F,
                            var change:Float  = 0F,
                            var high:Float  = 0F,
                            var low:Float  = 0F,
                            var volume:Long  = 0L,
                            var date:String = "",
                            var isFavorite :Boolean = false,
                            var isSelected :Boolean = false
)

//need to add no arg constructor

//Could not deserialize object. Class com.manish.stockapp.model.StockDetailsModel does not define a no-argument constructor.
