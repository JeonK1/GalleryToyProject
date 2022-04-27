package com.xemic.lorempicsum.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.xemic.lorempicsum.common.constant.ImagePageConst
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageLikeDao {
    @Insert
    suspend fun insert(imageLike: ImageLike)

    @Update
    suspend fun update(imageLike: ImageLike)

    /*** imageId 를 좋아요 눌렀는지 체크  ***
     * @param imageId : 이미지 고유 id
     * @return 0 = 좋아요 누르지 않았음
     * @return 1 = 좋아요 눌렀음
     * ***/
    @Query("SELECT COUNT(*) FROM ImageLike WHERE imageId = (:imageId) AND isLike")
    fun checkIsLike(imageId: String): Flow<Int>

    /*** imageId 에 해당하는 정보가 DB에 존재하는지 체크  ***
     * @param imageId : 이미지 고유 id
     * @return 0 = 데이터가 존재하지 않음
     * @return 1 = 데이터가 존재함
     * ***/
    @Query("SELECT COUNT(*) FROM ImageLike WHERE imageId = (:imageId)")
    fun checkIsExists(imageId: String): Flow<Int>

    /*** 좋아요 누른 이미지 리스트 page 단위로 가져오기  ***
     * @param page : 가져올 page 번호
     * @return 좋아요 누른 이미지 리스트
     * ***/
    @Query("SELECT * FROM ImageLike WHERE isLike LIMIT (((:page)-1) * ${ImagePageConst.LIMIT_IMAGE_PAGE}), ${ImagePageConst.LIMIT_IMAGE_PAGE}")
    fun getLikedImageList(page: Int): Flow<List<ImageLike>>
}