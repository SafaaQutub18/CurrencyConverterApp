package com.example.currencyconverterapp

import com.google.gson.annotations.SerializedName

class CurrencyData{
    @SerializedName("date")
    var date: String? = null
    @SerializedName("eur")
    var eur: Cur? = null

    class Cur {
        @SerializedName("inr")
        var inr: String? = null

        @SerializedName("aud")
        var aud: String? = null

        @SerializedName("ada")
        var ada: String? = null

        @SerializedName("all")
        var all: String? = null


    }
}