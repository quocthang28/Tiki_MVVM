package com.example.tikimvvm.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tikimvvm.db.entity.UserLocation

@Dao
interface UserLocationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserLocation(userLocation: UserLocation): Long
}