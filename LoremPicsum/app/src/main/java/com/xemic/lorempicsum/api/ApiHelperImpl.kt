package com.xemic.lorempicsum.api

import com.xemic.lorempicsum.common.constant.ImagePageConst
import com.xemic.lorempicsum.models.retrofit.Image
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    /*** image detail 정보 가져오기 ***/
    override suspend fun getImageInfo(id: String): Response<Image> = apiService.getImageInfo(id)

    /*** image list 가져오기 ***/
    override suspend fun getImageList(page: Int): Response<List<Image>> =
        apiService.getImageList(page, ImagePageConst.LIMIT_IMAGE_PAGE)
}