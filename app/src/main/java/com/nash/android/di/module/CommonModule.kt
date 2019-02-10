package com.nash.android.di.module

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonModule constructor(app: Application) {
    private var sApp: Application = app
    private var companyName = "Nash"

    @Provides
    @Singleton
    internal fun providesApplicationContext() = sApp.applicationContext

    @Provides
    @Singleton
    internal fun providesApplication() = sApp

    @Provides
    @Singleton
    internal fun providesBaseDatabaseReference() = FirebaseDatabase.getInstance().getReference(companyName)

    @Provides
    @Singleton
    internal fun providesFirebaseAuth() = FirebaseAuth.getInstance()


}