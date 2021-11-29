package com.manish.stockapp.di.module

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.manish.stockapp.util.Constants.FIREBASE_COLLECTION_PATH
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FireBaseModule {

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


    @Singleton
    @Provides
    fun provideCollection(fireStore: FirebaseFirestore): CollectionReference {
        return fireStore.collection(FIREBASE_COLLECTION_PATH)
    }
}