package com.example.bookapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookapp.model.Review as Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review where id==:id")
    fun getOneReview(id:Int): Review

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun saveReview(review:Review)
}