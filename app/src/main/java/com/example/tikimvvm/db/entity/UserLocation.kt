package com.example.tikimvvm.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_location")
data class UserLocation(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "location_id")
        val locationId: Int,

        val lat: Double,

        val lon: Double,

        val address: String,

        val numOfSatellite: Double,

        val hdop: Double,
)