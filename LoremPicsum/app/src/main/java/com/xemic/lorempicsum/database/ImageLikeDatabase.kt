package com.xemic.lorempicsum.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ImageLike::class), version = 1)
abstract class ImageLikeDatabase: RoomDatabase() {
    abstract fun imageLikeDao(): ImageLikeDao
}