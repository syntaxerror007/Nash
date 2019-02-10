package com.nash.android.setting

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.nash.android.core.CoreViewModel

class SettingViewModel : CoreViewModel() {
    private val isLoading = MutableLiveData<Boolean>()
    private val message = MutableLiveData<String>()

    fun isLoading() = isLoading as LiveData<Boolean>
    fun getMessageLiveData() = message as LiveData<String>

    fun changePassword(newPassword: String) {
        isLoading.value = true
        val user = getUser().value
        user?.updatePassword(newPassword)?.addOnCompleteListener {
            if (it.isSuccessful) {
                isLoading.value = false
                message.value = "Successfully change password"
            } else {
                message.value = "Failed to change password"
            }
        }
    }
}