package com.android.nash.core

import com.google.firebase.auth.FirebaseUser

interface BaseCoreActivity<T : CoreViewModel> {
    fun observeUser(it: FirebaseUser?)
    fun initViewModel()
    fun onCreateViewModel(): T
}