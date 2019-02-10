package com.nash.android.core.activity

import com.google.firebase.auth.FirebaseUser
import com.nash.android.core.CoreViewModel

interface BaseCoreActivity<T : CoreViewModel> {
    fun observeUser(it: FirebaseUser?)
    fun initViewModel()
    fun onCreateViewModel(): T
}