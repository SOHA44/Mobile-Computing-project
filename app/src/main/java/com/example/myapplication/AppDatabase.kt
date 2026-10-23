package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [User::class, Items::class, Claim::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): user_dao
    abstract fun itemDao(): ItemsDao
    abstract fun claimDao(): claim_dao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if(INSTANCE==null){
                synchronized(this)
                {
                    if(INSTANCE==null)
                    {
                        INSTANCE= Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database").build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
