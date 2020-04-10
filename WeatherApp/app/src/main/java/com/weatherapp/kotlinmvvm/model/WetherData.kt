package com.weatherapp.kotlinmvvm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WetherData {

    @SerializedName("cnt")
    @Expose
    private var cnt: Int? = null
    @SerializedName("list")
    @Expose
    private var list: kotlin.collections.List<List>? = null

    fun getCnt(): Int? {
        return cnt
    }

    fun setCnt(cnt: Int?) {
        this.cnt = cnt
    }

    fun getList(): kotlin.collections.List<List>? {
        return list
    }

    fun setList(list: kotlin.collections.List<List>) {
        this.list = list
    }

}