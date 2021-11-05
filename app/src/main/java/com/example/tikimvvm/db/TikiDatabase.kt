package com.example.tikimvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tikimvvm.db.dao.CategoryDAO
import com.example.tikimvvm.db.dao.UserLocationDAO
import com.example.tikimvvm.db.entity.Category
import com.example.tikimvvm.db.entity.UserLocation

@Database(entities = [Category::class, UserLocation::class], version = 1)
abstract class TikiDatabase : RoomDatabase() {
    abstract val categoryDAO: CategoryDAO
    abstract val userLocationDAO: UserLocationDAO

    companion object {
        @Volatile
        private var INSTANCE: TikiDatabase? = null

        fun getInstance(context: Context): TikiDatabase {
            synchronized(this) {
                var instance: TikiDatabase? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext, TikiDatabase::class.java,
                            "tiki_database"
                    ).build()
                }
                return instance
            }
        }
    }
}