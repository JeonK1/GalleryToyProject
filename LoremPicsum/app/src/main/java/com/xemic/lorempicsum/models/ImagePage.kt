package com.xemic.lorempicsum.models

/*** UI 에서 한 page 에서 보여주기  자료형 ***/
data class ImagePage(
    val imageList: List<ImageSimple> // 한 페이지에 포함된 이미지 리스트
) {
}