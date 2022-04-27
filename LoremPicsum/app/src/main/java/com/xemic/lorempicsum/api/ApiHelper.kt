package com.xemic.lorempicsum.api

import com.xemic.lorempicsum.models.retrofit.Image
import retrofit2.Response

interface ApiHelper {
    /*** image detail 정보 가져오기 ***/
    suspend fun getImageInfo(id: String):Response<Image>

    /*** image list 가져오기 ***/
    suspend fun getImageList(page: Int):Response<List<Image>>
}