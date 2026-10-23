package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Items::class], version = 1)
abstract class Itemdatabase: RoomDatabase(){
    abstract fun itemDao(): ItemsDao
    companion object{
        @Volatile
        private var INSTANCE: Itemdatabase?=null
        fun getInstance(context: Context): Itemdatabase{
            if (INSTANCE==null){
                synchronized(Itemdatabase::class){
                    INSTANCE= Room.databaseBuilder(context.applicationContext, Itemdatabase::class.java,"itemdb").build()
                }
            }
            return INSTANCE!!
        }
    }
}
