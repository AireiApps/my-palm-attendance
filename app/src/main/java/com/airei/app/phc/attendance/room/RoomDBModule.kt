package com.airei.app.phc.attendance.room

import android.content.Context
import com.airei.app.phc.attendance.room.dao.EmpAttendanceDao
import com.airei.app.phc.attendance.room.dao.EmployeeBioDao
import com.airei.app.phc.attendance.room.dao.EmployeeDao
import com.airei.app.phc.attendance.room.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomDBModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun provideEmployeeDao(database: AppDatabase): EmployeeDao = database.employeeDao()

    @Provides
    fun provideEmployeeBioDao(database: AppDatabase): EmployeeBioDao = database.employeeBioDao()

    @Provides
    fun provideEmpAttendanceDao(database: AppDatabase): EmpAttendanceDao = database.empAttendanceDao()
}