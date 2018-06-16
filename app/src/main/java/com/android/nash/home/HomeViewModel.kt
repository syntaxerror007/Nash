package com.android.nash.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.android.nash.core.CoreViewModel

class HomeViewModel:CoreViewModel() {
    private var adminMenus = mutableListOf("Register", "Log Out")
    private var cashierMenus = mutableListOf("Log Out")
    private val userMenu:LiveData<List<String>> = Transformations.map(getUserDataModel()) {
        if (it.userType.equals("Admin", true)) {
            return@map adminMenus
        }
        cashierMenus
    }

    fun getUserMenu(): LiveData<List<String>> {
        return userMenu
    }
}