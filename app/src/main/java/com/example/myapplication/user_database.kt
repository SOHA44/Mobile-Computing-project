//package com.example.myapplication
//import android.content.Context
//import androidx.room.RoomDatabase
//import androidx.room.Room
//import androidx.room.Database
//
//@Database
//(entities = [User::class], version = 1)
//abstract class user_database : RoomDatabase() {
//    abstract fun userDao(): user_dao
//
//    companion object{
//        @Volatile
//        private var INSTANCE: user_database? = null
//        fun getinstance(context: Context): user_database {
//          if (INSTANCE == null) {
//                synchronized(this) {
//                    INSTANCE = Room.databaseBuilder(
//                        context.applicationContext,
//                        user_database::class.java,
//                        "user_database"
//                    ).build()
//                }
//            }
//            return INSTANCE!!
//            }
//        }
//    }
//
//
