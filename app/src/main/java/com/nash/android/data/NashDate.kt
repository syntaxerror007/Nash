package com.nash.android.data

import org.parceler.Parcel

@Parcel(Parcel.Serialization.BEAN)
data class NashDate(var day: Int = 1, var month: Int = 1, var year: Int = 2018, var hour: Int = 0, var minute: Int = 0)