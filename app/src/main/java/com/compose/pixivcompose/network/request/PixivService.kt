package com.compose.pixivcompose.network.request

import com.compose.pixivcompose.network.response.ApiResponse
import com.compose.pixivcompose.network.response.ResponsePicBean
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixivService {

  @GET("setu/v2")
  suspend fun fetchRandomPics(
    @Query("r18") r18: Boolean = false,
    @Query("num") num: Int = 20,
    @Query("excludeAI") excludeAI: Boolean = false
  ): Response<ApiResponse<MutableList<ResponsePicBean>>>
}
