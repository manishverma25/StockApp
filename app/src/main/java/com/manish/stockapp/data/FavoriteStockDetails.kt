package com.manish.stockapp.data

data class FavoriteStockDetails(
    var sid: String = "",
    var isfavorite: Boolean = false
    /**
     *
     * isfavorite  is added so it can be used to implement to do  unfavorite the stock  functionality
     *
     */
)


