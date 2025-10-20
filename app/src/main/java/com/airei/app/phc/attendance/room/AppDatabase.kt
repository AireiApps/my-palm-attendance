package com.airei.app.phc.attendance.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.airei.app.phc.attendance.entity.*
import com.airei.app.phc.attendance.room.dao.*

@Database(
    entities = [
        // Plantation tables
        UserTable::class,
        EmployeeTable::class,
        EmployeeBioTable::class,
        EmpAttendanceTable::class,
        EstateEntity::class,
        DivisionEntity::class,
        BlockEntity::class,
        ParcelEntity::class
    ],
    version = 1, // No version bump
    exportSchema = false
)
@TypeConverters(FloatArrayConverter::class)
abstract class AppDatabase : RoomDatabase() {

    // Plantation DAOs
    abstract fun userDao(): UserDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun employeeBioDao(): EmployeeBioDao
    abstract fun empAttendanceDao(): EmpAttendanceDao
    abstract fun estateDao(): EstateDao
    abstract fun divisionDao(): DivisionDao
    abstract fun blockDao(): BlockDao
    abstract fun parcelDao(): ParcelDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "attendance_phc_db"
            )
                .allowMainThreadQueries()
                .build()

        }
    }
}