package com.android.nash.data

import org.parceler.Parcel

@Parcel(Parcel.Serialization.BEAN)
data class NashDate(var day: Int = 1, var month: Int = 1, var year: Int = 2018)