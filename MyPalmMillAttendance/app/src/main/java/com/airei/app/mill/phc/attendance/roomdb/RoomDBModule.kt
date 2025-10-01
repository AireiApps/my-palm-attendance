package com.airei.app.plantation.phc.roomdb

import android.content.Context
import com.airei.app.mill.phc.attendance.roomdb.AppDatabase
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
    //LoginUserDao
    @Provides
    fun provideLoginUserDao(database: AppDatabase): LoginUserDao {
        return database.loginUserDao()
    }
    //EmpTableDao
    @Provides
    fun provideEmpTableDao(database: AppDatabase): EmpTableDao {
        return database.employeeDao()
    }

    @Provides
    fun provideEmployeeBioDao(database: AppDatabase): EmployeeBioDao {
        return database.employeeBioDao()
    }

    @Provides
    fun provideEmpAttendanceDao(database: AppDatabase): EmpAttendanceDao {
        return database.empAttendanceDao()
    }

    @Provides
    fun provideEstateDao(database: AppDatabase): EstateDao {
        return database.estateDao()
    }

    @Provides
    fun provideDivisionDao(database: AppDatabase): DivisionDao {
        return database.divisionDao()
    }

    @Provides
    fun provideBlockDao(database: AppDatabase): BlockDao {
        return database.blockDao()
    }

    @Provides
    fun provideParcelDao(database: AppDatabase): ParcelDao {
        return database.parcelDao()
    }

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideActivityDao(database: AppDatabase): ActivityDao {
        return database.activityDao()
    }

    @Provides
    fun provideChemicalDao(database: AppDatabase): ChemicalDao {
        return database.chemicalDao()
    }

    @Provides
    fun provideDriverDao(database: AppDatabase): DriverDao {
        return database.driverDao()
    }
    @Provides
    fun provideCoreActivityDao(database: AppDatabase): CoreActivityDao {
        return database.coreActivityDao()
    }

    @Provides
    fun provideEmpGroupDao(database: AppDatabase): EmpGroupDao {
        return database.empGroupDao()
    }

    @Provides
    fun provideJobIdListDao(database: AppDatabase): JobIdListDao {
        return database.jobIdListDao()
    }

    @Provides
    fun provideFertilizerDao(database: AppDatabase): FertilizerDao {
        return database.fertilizerDao()
    }

    @Provides
    fun providePestDiseaseDao(database: AppDatabase): PestDiseaseDao {
        return database.pestDiseaseDao()
    }

    @Provides
    fun provideVehicleDao(database: AppDatabase): VehicleDao {
        return database.vehicleDao()
    }

    @Provides
    fun provideActivityCalenderDao(database: AppDatabase): ActivityCalenderDao {
        return database.activityCalenderDao()
    }

    // Record
    @Provides
    fun provideHarvestDataDao(database: AppDatabase): HarvestDataDao {
        return database.harvestDataDao()
    }
    @Provides
    fun provideLooseFruitDataDao(database: AppDatabase): LooseFruitDataDao {
        return database.looseFruitDataDao()
    }

    @Provides
    fun provideQualityCheckDataDao(database: AppDatabase): QualityCheckDataDao {
        return database.qualityCheckDataDao()
    }

    @Provides
    fun provideCropLossDataDao(database: AppDatabase): CropLossDataDao {
        return database.cropLossDataDao()
    }

    @Provides
    fun provideEvacuationDataDao(database: AppDatabase): EvacuationDataDao {
        return database.evacuationDataDao()
    }

    @Provides
    fun provideOtherActivityDataDao(database: AppDatabase): OtherActivityDataDao {
        return database.otherActivityDataDao()
    }


    /*RingWeedingDataDao
    CircleWeedingDataDao
    PruningDataDao
    FertilizingDataDao
    RoadMaintenanceDataDao
    PestDiseasesDataDao
    RingSprayingDataDao
    SelectiveSprayingDataDao
    CensusDataDao
    MechWeedingDataDao
    ChemicalWeedingDataDao*/
    @Provides
    fun provideRingWeedingDataDao(database: AppDatabase): RingWeedingDataDao {
        return database.ringWeedingDataDao()
    }

    @Provides
    fun provideCircleWeedingDataDao(database: AppDatabase): CircleWeedingDataDao {
        return database.circleWeedingDataDao()
    }

    @Provides
    fun providePruningDataDao(database: AppDatabase): PruningDataDao {
        return database.pruningDataDao()
    }

    @Provides
    fun provideFertilizingDataDao(database: AppDatabase): FertilizingDataDao {
        return database.fertilizingDataDao()
    }

    @Provides
    fun provideRoadMaintenanceDataDao(database: AppDatabase): RoadMaintenanceDataDao {
        return database.roadMaintenanceDataDao()
    }

    @Provides
    fun providePestDiseasesDataDao(database: AppDatabase): PestDiseasesDataDao {
        return database.pestDiseasesDataDao()
    }

    @Provides
    fun provideRingSprayingDataDao(database: AppDatabase): RingSprayingDataDao {
        return database.ringSprayingDataDao()
    }

    @Provides
    fun provideSelectiveSprayingDataDao(database: AppDatabase): SelectiveSprayingDataDao {
        return database.selectiveSprayingDataDao()
    }

    @Provides
    fun provideCensusDataDao(database: AppDatabase): CensusDataDao {
        return database.censusDataDao()
    }

    @Provides
    fun provideMechWeedingDataDao(database: AppDatabase): MechWeedingDataDao {
        return database.mechWeedingDataDao()
    }

    @Provides
    fun provideChemicalWeedingDataDao(database: AppDatabase): ChemicalWeedingDataDao {
        return database.chemicalWeedingDataDao()
    }









}