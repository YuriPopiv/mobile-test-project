package com.accelo.data.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by Yuri Popiv on 12/3/2019.
 */
@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities")
    fun getActivities(): Flowable<List<PendingActivity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActivity(activity: PendingActivity): Completable

    @Query("DELETE FROM activities")
    fun deleteActivities()

}