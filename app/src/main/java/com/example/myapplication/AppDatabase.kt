package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [User::class, Items::class, Claim::class, NotificationEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): user_dao
    abstract fun itemDao(): ItemsDao
    abstract fun claimDao(): claim_dao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `notifications` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `userId` INTEGER NOT NULL,
                        `title` TEXT NOT NULL,
                        `message` TEXT NOT NULL,
                        `type` TEXT NOT NULL,
                        `itemId` INTEGER,
                        `createdAt` INTEGER NOT NULL,
                        `isRead` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN image TEXT")
            }
        }

        fun getInstance(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this)
            {
                INSTANCE ?: Room.databaseBuilder(context.applicationContext,AppDatabase::class.java, "app_database")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration(true)
                    .build().also { INSTANCE = it }
            }
        }
    }
}
