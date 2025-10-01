package com.airei.app.mill.phc.attendance.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.airei.app.mill.phc.attendance.entity.ActivityCalenderTable
import com.airei.app.mill.phc.attendance.entity.ActivityTable
import com.airei.app.mill.phc.attendance.entity.BlockTable
import com.airei.app.mill.phc.attendance.entity.CensusData
import com.airei.app.mill.phc.attendance.entity.ChemicalWeedingData
import com.airei.app.mill.phc.attendance.entity.ChemicalTable
import com.airei.app.mill.phc.attendance.entity.CircleWeedingData
import com.airei.app.mill.phc.attendance.entity.CoreActivityTable
import com.airei.app.mill.phc.attendance.entity.CropLossData
import com.airei.app.mill.phc.attendance.entity.DivisionTable
import com.airei.app.mill.phc.attendance.entity.DriverTable
import com.airei.app.mill.phc.attendance.entity.EmpAttendanceTable
import com.airei.app.mill.phc.attendance.entity.EmpGroupTable
import com.airei.app.mill.phc.attendance.entity.EmployeeBioTable
import com.airei.app.mill.phc.attendance.entity.EmployeeTable
import com.airei.app.mill.phc.attendance.entity.EstateTable
import com.airei.app.mill.phc.attendance.entity.EvacuationData
import com.airei.app.mill.phc.attendance.entity.FertilizerTable
import com.airei.app.mill.phc.attendance.entity.FertilizingData
import com.airei.app.mill.phc.attendance.entity.HarvestData
import com.airei.app.mill.phc.attendance.entity.JobIdList
import com.airei.app.mill.phc.attendance.entity.LoginUserTable
import com.airei.app.mill.phc.attendance.entity.LooseFruitData
import com.airei.app.mill.phc.attendance.entity.MechWeedingData
import com.airei.app.mill.phc.attendance.entity.OtherActivityData
import com.airei.app.mill.phc.attendance.entity.ParcelTable
import com.airei.app.mill.phc.attendance.entity.PestDiseaseTable
import com.airei.app.mill.phc.attendance.entity.PestDiseasesData
import com.airei.app.mill.phc.attendance.entity.PruningData
import com.airei.app.mill.phc.attendance.entity.QualityCheckData
import com.airei.app.mill.phc.attendance.entity.RingSprayingData
import com.airei.app.mill.phc.attendance.entity.RingWeedingData
import com.airei.app.mill.phc.attendance.entity.RoadMaintenanceData
import com.airei.app.mill.phc.attendance.entity.SelectiveSprayingData
import com.airei.app.mill.phc.attendance.entity.TaskTable
import com.airei.app.mill.phc.attendance.entity.VehicleTable


@Database(
    entities = [
        LoginUserTable::class,
        EmployeeTable::class,
        EmpGroupTable::class,
        EmployeeBioTable::class,
        EmpAttendanceTable::class,
        EstateTable::class,
        DivisionTable::class,
        BlockTable::class,
        ParcelTable::class,
        TaskTable::class,
        ActivityTable::class,
        FertilizerTable::class,
        PestDiseaseTable::class,
        ChemicalTable::class,
        DriverTable::class,
        CoreActivityTable::class,

        VehicleTable::class,
        ActivityCalenderTable::class,

        JobIdList::class,

        HarvestData::class,
        LooseFruitData::class,
        QualityCheckData::class,
        CropLossData::class,
        EvacuationData::class,
        OtherActivityData::class,

        RingWeedingData::class,
        CircleWeedingData::class,
        PruningData::class,
        FertilizingData::class,
        RoadMaintenanceData::class,
        PestDiseasesData::class,
        RingSprayingData::class,
        SelectiveSprayingData::class,
        CensusData::class,
        MechWeedingData::class,
        ChemicalWeedingData::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DataTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun loginUserDao(): LoginUserDao

    abstract fun employeeDao(): EmpTableDao
    abstract fun empGroupDao(): EmpGroupDao
    abstract fun employeeBioDao(): EmployeeBioDao
    abstract fun empAttendanceDao(): EmpAttendanceDao
    abstract fun estateDao(): EstateDao
    abstract fun divisionDao(): DivisionDao
    abstract fun blockDao(): BlockDao
    abstract fun parcelDao(): ParcelDao
    abstract fun taskDao(): TaskDao
    abstract fun activityDao(): ActivityDao
    abstract fun fertilizerDao(): FertilizerDao
    abstract fun pestDiseaseDao(): PestDiseaseDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun activityCalenderDao(): ActivityCalenderDao
    abstract fun chemicalDao(): ChemicalDao
    abstract fun driverDao(): DriverDao
    abstract fun coreActivityDao(): CoreActivityDao

    abstract fun jobIdListDao(): JobIdListDao

    //prodection
    abstract fun harvestDataDao(): HarvestDataDao
    abstract fun looseFruitDataDao(): LooseFruitDataDao
    abstract fun qualityCheckDataDao(): QualityCheckDataDao
    abstract fun cropLossDataDao(): CropLossDataDao
    abstract fun evacuationDataDao(): EvacuationDataDao
    abstract fun otherActivityDataDao(): OtherActivityDataDao

    //Maintenance
    abstract fun ringWeedingDataDao(): RingWeedingDataDao
    abstract fun circleWeedingDataDao(): CircleWeedingDataDao
    abstract fun pruningDataDao(): PruningDataDao
    abstract fun fertilizingDataDao(): FertilizingDataDao
    abstract fun roadMaintenanceDataDao(): RoadMaintenanceDataDao
    abstract fun pestDiseasesDataDao(): PestDiseasesDataDao
    abstract fun ringSprayingDataDao(): RingSprayingDataDao
    abstract fun selectiveSprayingDataDao(): SelectiveSprayingDataDao
    abstract fun censusDataDao(): CensusDataDao
    abstract fun mechWeedingDataDao(): MechWeedingDataDao
    abstract fun chemicalWeedingDataDao(): ChemicalWeedingDataDao



    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, "my_palm_phc_db"
            ).allowMainThreadQueries().build()
        }
    }
}