package com.matekids.di

import android.content.Context
import androidx.room.Room
import com.matekids.data.local.MateKidsDatabase
import com.matekids.data.local.dao.AchievementDao
import com.matekids.data.local.dao.OperationDao
import com.matekids.data.local.dao.ProblemDao
import com.matekids.data.local.dao.UserDao
import com.matekids.data.repository.AchievementRepository
import com.matekids.data.repository.OperationRepository
import com.matekids.data.repository.ProblemRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.usecase.CalculateProgressUseCase
import com.matekids.domain.usecase.GetAchievementsUseCase
import com.matekids.domain.usecase.ResolveOperationUseCase
import com.matekids.domain.usecase.ResolveProblemUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMateKidsDatabase(@ApplicationContext context: Context): MateKidsDatabase {
        return Room.databaseBuilder(
            context,
            MateKidsDatabase::class.java,
            "matekids.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideOperationDao(database: MateKidsDatabase): OperationDao {
        return database.operationDao()
    }

    @Singleton
    @Provides
    fun provideProblemDao(database: MateKidsDatabase): ProblemDao {
        return database.problemDao()
    }

    @Singleton
    @Provides
    fun provideUserDao(database: MateKidsDatabase): UserDao {
        return database.userDao()
    }

    @Singleton
    @Provides
    fun provideAchievementDao(database: MateKidsDatabase): AchievementDao {
        return database.achievementDao()
    }

    @Singleton
    @Provides
    fun provideOperationRepository(operationDao: OperationDao): OperationRepository {
        return OperationRepository(operationDao)
    }

    @Singleton
    @Provides
    fun provideProblemRepository(problemDao: ProblemDao): ProblemRepository {
        return ProblemRepository(problemDao)
    }

    @Singleton
    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }

    @Singleton
    @Provides
    fun provideAchievementRepository(achievementDao: AchievementDao): AchievementRepository {
        return AchievementRepository(achievementDao)
    }

    @Singleton
    @Provides
    fun provideResolveOperationUseCase(
        operationRepository: OperationRepository,
        userRepository: UserRepository
    ): ResolveOperationUseCase {
        return ResolveOperationUseCase(operationRepository, userRepository)
    }

    @Singleton
    @Provides
    fun provideResolveProblemUseCase(
        problemRepository: ProblemRepository,
        userRepository: UserRepository
    ): ResolveProblemUseCase {
        return ResolveProblemUseCase(problemRepository, userRepository)
    }

    @Singleton
    @Provides
    fun provideCalculateProgressUseCase(
        operationRepository: OperationRepository,
        problemRepository: ProblemRepository,
        userRepository: UserRepository
    ): CalculateProgressUseCase {
        return CalculateProgressUseCase(operationRepository, problemRepository, userRepository)
    }

    @Singleton
    @Provides
    fun provideGetAchievementsUseCase(
        achievementRepository: AchievementRepository
    ): GetAchievementsUseCase {
        return GetAchievementsUseCase(achievementRepository)
    }
}
