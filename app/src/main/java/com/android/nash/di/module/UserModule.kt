package com.android.nash.di.module

import com.android.nash.provider.UserProvider
import dagger.Module
import dagger.Provides

@Module
class UserModule {
    @Provides
    fun getUserProvider():UserProvider = UserProvider()
}