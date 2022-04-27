package com.xemic.lorempicsum.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ImageLike(
    @PrimaryKey
    @ColumnInfo(name = "imageId") val imageId: String,          // 고유 id
    @ColumnInfo(name = "author") val author: String,            // 이미지 주인
    @ColumnInfo(name = "width") val width: Int,                 // original image width
    @ColumnInfo(name = "height") val height: Int,               // original image height
    @ColumnInfo(name = "url") val url: String,                  // original image url
    @ColumnInfo(name = "download_url") val download_url: String, // download image url
    @ColumnInfo(name = "isLike") val isLike: Boolean            // 좋아요 눌렀는지
) {
}


