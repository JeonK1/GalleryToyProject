package com.xemic.lorempicsum.api

import com.xemic.lorempicsum.models.retrofit.Image
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /***
     * Image Details
     * @param id : 이미지의 고유 id
     * ***/
    @GET("/id/{id}/info")
    suspend fun getImageInfo(
        @Path("id") id:String
    ):Response<Image>

    /***
     * List Images
     * @param page : 가져올 이미지 리스트의 page
     * @param limit : 한 page 에 포함된 최대 개수
     * ***/
    @GET("/v2/list")
    suspend fun getImageList(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ):Response<List<Image>>
}