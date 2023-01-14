package com.compose.pixivcompose.network.response

import com.google.gson.annotations.SerializedName

data class ResponseImgBean(
  @SerializedName("aiType") val aiType: Int,
  @SerializedName("author") val author: String,
  @SerializedName("ext") val ext: String,
  @SerializedName("height") val height: Int,
  @SerializedName("p") val p: Int,
  @SerializedName("pid") val pid: Int,
  @SerializedName("r18") val r18: Boolean,
  @SerializedName("tags") val tags: List<Any>,
  @SerializedName("title") val title: String,
  @SerializedName("uid") val uid: Int,
  @SerializedName("uploadDate") val uploadDate: Long,
  @SerializedName("urls") val urls: Urls,
  @SerializedName("width") val width: Int
) : java.io.Serializable

data class Urls(
  @SerializedName("original") val original: String?,
  @SerializedName("regular") val regular: String?,
  @SerializedName("small") val small: String?,
  @SerializedName("thumb") val thumb: String?,
  @SerializedName("mini") val mini: String?
) : java.io.Serializable
