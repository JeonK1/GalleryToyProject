package com.xemic.lorempicsum.common.ext

import android.widget.ImageView
import com.bumptech.glide.Glide

/*** ImageView에 Glide + URL 이용하여, 이미지 넣어주기  ***/
fun ImageView.setImageByUrl(url:String){
    Glide.with(this.context)
        .load(url)
        .into(this)
}