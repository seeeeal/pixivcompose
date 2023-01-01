package com.compose.pixivcompose.network.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(@SerializedName("error") val error: String, @SerializedName("data") val data: T) :
  java.io.Serializable
