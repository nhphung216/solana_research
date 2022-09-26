package com.example.myapplication.response

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("value") var value: Value? = Value()
): BaseModel() {

    companion object {
        @JvmStatic
        fun fromJSON(json: String): Result {
            return GsonBuilder().create().fromJson(json, Result::class.java)
        }
    }
}