package com.xemic.lorempicsum.di

import android.app.Application
import androidx.room.Room
import com.xemic.lorempicsum.database.ImageLikeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        app:Application
    ) = Room.databaseBuilder(app, ImageLikeDatabase::class.java, "image_database")
        .build()

    @Provides
    @Singleton
    fun provideImageLikeDao(db: ImageLikeDatabase) = db.imageLikeDao()
}