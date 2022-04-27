package com.xemic.lorempicsum.models.retrofit

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id") val id: String,                       // 이미지 고유 id
    @SerializedName("author") val author: String,               // 이미지 주인
    @SerializedName("width") val width: Int,                    // original image width
    @SerializedName("height") val height: Int,                  // original image height
    @SerializedName("url") val url: String,                     // original image url
    @SerializedName("download_url") val download_url: String,   // download image url
)
