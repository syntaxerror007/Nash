package com.android.nash.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel

class HomeViewModel: CoreViewModel() {
    private var adminMenus = mutableListOf("Register User", "Register Location", "Services", "Log Out")
    private var cashierMenus = mutableListOf("Log Out")
//    private val userMenu:LiveData<List<String>> = Transformations.map(getUserDataModel()) {
//        if (it.userType.equals("Admin", true)) {
//            return@map adminMenus
//        }
//        cashierMenus
//    }
    private var userMenu = MutableLiveData<List<String>>()

    fun getUserMenu(): LiveData<List<String>> {
        userMenu.value = adminMenus
        return userMenu
    }
}