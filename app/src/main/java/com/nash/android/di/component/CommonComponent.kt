package com.nash.android.di.component

import android.app.Application
import android.content.Context
import com.nash.android.di.module.CommonModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CommonModule::class])
interface CommonComponent {
    fun getApplicationContext(): Context
    fun getApplication(): Application
}