package com.example.myapplication.response

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

data class Value(
    @SerializedName("data") var data: ArrayList<String> = arrayListOf()
) : BaseModel() {

    companion object {
        @JvmStatic
        fun fromJSON(json: String): Value {
            return GsonBuilder().create().fromJson(json, Value::class.java)
        }
    }
}
