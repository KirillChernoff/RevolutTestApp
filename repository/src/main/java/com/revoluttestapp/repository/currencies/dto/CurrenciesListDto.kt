package com.revoluttestapp.repository.currencies.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class CurrenciesListDto(
    @SerializedName("base")
    val baseCurrency: String,

    @SerializedName("date")
    val date: Date,

    @SerializedName("rates")
    val rates: Map<String, String>
)

