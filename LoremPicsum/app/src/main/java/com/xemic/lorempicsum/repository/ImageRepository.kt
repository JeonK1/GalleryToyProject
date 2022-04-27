package com.xemic.lorempicsum.repository

import com.xemic.lorempicsum.api.ApiHelper
import com.xemic.lorempicsum.database.ImageLike
import com.xemic.lorempicsum.database.ImageLikeDao
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val imageLikeDao: ImageLikeDao
) {
    // get data by Network
    suspend fun getImageInfo(id: String) = apiHelper.getImageInfo(id)
    suspend fun getImageList(page: Int) = apiHelper.getImageList(page)

    // get data by Local DB
    suspend fun insertImageLike(imageLike : ImageLike) = imageLikeDao.insert(imageLike)
    suspend fun updateImageLike(imageLike : ImageLike) = imageLikeDao.update(imageLike)
    fun checkIsLike(imageId: String) = imageLikeDao.checkIsLike(imageId)
    fun checkIsExists(imageId: String) = imageLikeDao.checkIsExists(imageId)
    fun getLikedImageList(page: Int) = imageLikeDao.getLikedImageList(page)
}