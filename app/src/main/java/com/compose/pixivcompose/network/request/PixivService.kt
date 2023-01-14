package com.compose.pixivcompose.network.request

import com.compose.pixivcompose.network.response.ApiResponse
import com.compose.pixivcompose.network.response.ResponseImgBean
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixivService {

  @GET("setu/v2")
  suspend fun fetchRandomImages(
    @Query("r18") r18: Boolean = false,
    @Query("num") num: Int = 20,
    @Query("excludeAI") excludeAI: Boolean = false,
    @Query("size") size: Array<String> = arrayOf("original", "regular", "small")
  ): Response<ApiResponse<List<ResponseImgBean>>>
}
