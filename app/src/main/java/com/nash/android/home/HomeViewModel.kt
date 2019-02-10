package com.nash.android.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.nash.android.core.CoreViewModel

class HomeViewModel : CoreViewModel() {
    private val isLoadingLiveData = MutableLiveData<Boolean>()

    fun isLoadingLiveData(): LiveData<Boolean> = isLoadingLiveData

    fun setLoading(isLoading: Boolean) {
        isLoadingLiveData.value = isLoading
    }
}