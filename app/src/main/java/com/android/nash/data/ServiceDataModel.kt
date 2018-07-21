package com.android.nash.data

data class ServiceDataModel(var id:String, var serviceName:String, var defaultPrice:Long, var reminder:Int, var shouldFreeText:Boolean) {
    constructor(): this("","",0,0, false)
}