package com.nash.android.di.module

import com.nash.android.provider.UserProvider
import dagger.Module
import dagger.Provides

@Module
class UserModule {
    @Provides
    fun getUserProvider(): UserProvider = UserProvider()
}