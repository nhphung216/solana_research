package com.example.myapplication.response

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

data class SolanaDataResponse(
    @SerializedName("result") var result: Result? = Result()
) : BaseModel() {

    companion object {
        @JvmStatic
        fun fromJSON(json: String): SolanaDataResponse {
            return GsonBuilder().create().fromJson(json, SolanaDataResponse::class.java)
        }
    }
}