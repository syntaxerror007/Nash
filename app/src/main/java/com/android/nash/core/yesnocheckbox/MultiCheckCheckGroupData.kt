package com.android.nash.core.yesnocheckbox

data class MultiCheckCheckGroupData(val id:String = "", val title: String = "", val options: List<String> = listOf("Yes", "No"), val enableMultiCheck: Boolean = false)