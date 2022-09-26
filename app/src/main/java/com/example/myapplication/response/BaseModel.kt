package com.example.myapplication.response

import com.google.gson.Gson
import java.io.Serializable

abstract class BaseModel : Serializable {
    fun toJSON(): String {
        return gson.toJson(this)
    }

    companion object {
        protected val gson = Gson()
    }
}