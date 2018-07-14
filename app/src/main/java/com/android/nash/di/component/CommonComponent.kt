package com.android.nash.di.component

import android.app.Application
import android.content.Context
import com.android.nash.di.module.CommonModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CommonModule::class])
interface CommonComponent {
    fun getApplicationContext(): Context
    fun getApplication(): Application
}