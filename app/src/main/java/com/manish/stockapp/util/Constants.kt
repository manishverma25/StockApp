package com.manish.stockapp.util

object Constants{
    const val BASE_URL = "https://api.tickertape.in"

    const val  STOCK_DEATILS_API_PERIODIC_TIMER = 2500L // in milliseconds
    const val FIREBASE_COLLECTION_PATH ="StockDB"
    const val FIREBASE_DOCUMENT_NAME ="Stock Details"
    const val FIREBASE_DOCUMENT_PATH = "$FIREBASE_COLLECTION_PATH/$FIREBASE_DOCUMENT_NAME"

    const val  KEY_FIELD_FOR_FAVORITE =  "favorite" //todo revert it to favorite
}