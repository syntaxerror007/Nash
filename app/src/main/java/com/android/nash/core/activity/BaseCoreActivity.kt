package com.android.nash.core.activity

import com.android.nash.core.CoreViewModel
import com.google.firebase.auth.FirebaseUser

interface BaseCoreActivity<T : CoreViewModel> {
    fun observeUser(it: FirebaseUser?)
    fun initViewModel()
    fun onCreateViewModel(): T
}