package com.matekids.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.matekids.data.local.dao.AchievementDao
import com.matekids.data.local.dao.OperationDao
import com.matekids.data.local.dao.ProblemDao
import com.matekids.data.local.dao.UserDao
import com.matekids.data.local.entity.AchievementEntity
import com.matekids.data.local.entity.OperationEntity
import com.matekids.data.local.entity.ProblemEntity
import com.matekids.data.local.entity.UserProfileEntity

@Database(
    entities = [
        OperationEntity::class,
        ProblemEntity::class,
        UserProfileEntity::class,
        AchievementEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MateKidsDatabase : RoomDatabase() {
    abstract fun operationDao(): OperationDao
    abstract fun problemDao(): ProblemDao
    abstract fun userDao(): UserDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile
        private var instance: MateKidsDatabase? = null

        fun getInstance(context: Context): MateKidsDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MateKidsDatabase::class.java,
                    "matekids.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
