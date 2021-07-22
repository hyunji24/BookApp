package com.example.bookapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookapp.dao.HistoryDao
import com.example.bookapp.model.History

@Database(entities=[History::class],version=1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun historyDao(): HistoryDao //db는 dao에서 꺼내옴
}