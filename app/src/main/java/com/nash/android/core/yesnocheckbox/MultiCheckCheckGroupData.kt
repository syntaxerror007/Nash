package com.nash.android.core.yesnocheckbox

data class MultiCheckCheckGroupData(val id: String = "", val title: String = "", val options: List<String> = listOf("Yes", "No"), val enableMultiCheck: Boolean = false)