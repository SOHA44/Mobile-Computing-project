package com.example.myapplication
@database(entities = [User::class], version = 1)
abstract class user_database {
    abstract fun userDao(): user_dao

    companion object{
        @Volatile
        private var INSTANCE: user_database? = null
        fun getinstance(context: Context): user_database {
          if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        user_database::class.java,
                        "user_database"
                    ).build()
                }
            }
            return INSTANCE!!
            }
        }
    }


