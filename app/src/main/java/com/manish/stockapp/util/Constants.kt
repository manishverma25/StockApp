package com.manish.stockapp.util

object Constants{
    const val BASE_URL = "https://api.tickertape.in"
    const val BASE_URL_2 = "https://reqres.in"

    const val FIREBASE_COLLECTION_PATH ="StockDB"
    const val FIREBASE_DOCUMENT_NAME ="Stock Details"
    const val FIREBASE_DOCUMENT_PATH = "$FIREBASE_COLLECTION_PATH/$FIREBASE_DOCUMENT_NAME"
}