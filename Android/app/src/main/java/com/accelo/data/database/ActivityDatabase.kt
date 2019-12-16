package com.accelo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by Yuri Popiv on 12/3/2019.
 */
@Database(entities = arrayOf(PendingActivity::class), version = 1)
abstract class ActivityDatabase : RoomDatabase(){

    abstract fun activityDao() : ActivityDao

    companion object{

        @Volatile private var INSTANCE: ActivityDatabase? = null

        fun getInstance(context: Context): ActivityDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also{ INSTANCE = it}
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                ActivityDatabase::class.java, "activities.db").build()
    }
}