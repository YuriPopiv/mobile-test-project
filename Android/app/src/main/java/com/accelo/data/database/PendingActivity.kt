package com.accelo.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Yuri Popiv on 12/3/2019.
 */
@Entity(tableName = "activities")
data class PendingActivity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Long,
    @ColumnInfo(name ="against_id")
    val againstId: Int,
    @ColumnInfo(name ="against_type")
    val againstType: String,
    @ColumnInfo(name ="medium")
    val medium: String,
    @ColumnInfo(name ="owner_type")
    val ownerType: String,
    @ColumnInfo(name ="visibility")
    val visibility: String,
    @ColumnInfo(name ="subject")
    val subject: String,
    @ColumnInfo(name ="body")
    val body: String
)