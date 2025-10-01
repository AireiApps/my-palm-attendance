package com.airei.app.mill.phc.attendance.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.airei.app.mill.phc.attendance.entity.ActivityCalenderTable
import com.airei.app.mill.phc.attendance.entity.ActivityTable
import com.airei.app.mill.phc.attendance.entity.BlockTable
import com.airei.app.mill.phc.attendance.entity.CensusData
import com.airei.app.mill.phc.attendance.entity.ChemicalTable
import com.airei.app.mill.phc.attendance.entity.ChemicalWeedingData
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

@Dao
interface LoginUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoginUser(data: LoginUserTable)

    @Update
    suspend fun updateLoginUser(data: LoginUserTable)

    @Delete
    suspend fun deleteLoginUser(data: LoginUserTable)

    @Query("SELECT * FROM user_table")
    fun getAllLoginUser(): LiveData<List<LoginUserTable>>
}

@Dao
interface EmpTableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmpTable(data: EmployeeTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmpTableList(data: List<EmployeeTable>)

    @Update
    suspend fun updateEmpTable(data: EmployeeTable)

    @Delete
    suspend fun deleteEmpTable(data: EmployeeTable)

    @Query("DELETE FROM employee_table")
    fun deleteAllEmpTable()

    @Query("SELECT * FROM employee_table")
    fun getAllEmpTable(): LiveData<List<EmployeeTable>>
}

@Dao
interface EmployeeBioDao {
    @Query("SELECT * FROM emp_bio_table")
    fun getAllEmployeeBio(): LiveData<List<EmployeeBioTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmpBio(data: EmployeeBioTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmpBioList(data: List<EmployeeBioTable>)

    @Delete
    suspend fun deleteEmpBio(data: EmployeeBioTable)

    @Update
    suspend fun updateEmpBio(data: EmployeeBioTable)

    @Query("DELETE FROM emp_bio_table")
    fun deleteAllEmpBio()



}


@Dao
interface EmpAttendanceDao {
    @Query("SELECT * FROM emp_attendance_table")
    fun getAllEmpAttendance(): LiveData<List<EmpAttendanceTable>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmpAttendance(data: EmpAttendanceTable)
    @Delete
    suspend fun deleteEmpAttendance(data: EmpAttendanceTable)
    @Update
    suspend fun updateEmpAttendance(data: EmpAttendanceTable)
}

@Dao
interface EstateDao {
    @Query("SELECT * FROM estate_table")
    fun getAllEstates(): LiveData<List<EstateTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstates(data: List<EstateTable>)


    @Query("DELETE FROM estate_table WHERE login_id = :id")
    suspend fun deleteEstates(id: String)
}

@Dao
interface DivisionDao {
    @Query("SELECT * FROM division_table")
    fun getAllDivision(): LiveData<List<DivisionTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDivisions(data: List<DivisionTable>)

    @Query("DELETE FROM division_table WHERE login_id = :id")
    suspend fun deleteDivisions(id: String)
}

@Dao
interface BlockDao {
    @Query("SELECT * FROM block_table")
    fun getAllBlock(): LiveData<List<BlockTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlocks(data: List<BlockTable>)

    @Query("DELETE FROM block_table WHERE login_id = :id")
    suspend fun deleteBlocks(id: String)
}

//Parcel
@Dao
interface ParcelDao {
    @Query("SELECT * FROM parcel_table")
    fun getAllParcel(): LiveData<List<ParcelTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParcel(data: List<ParcelTable>)

    @Query("DELETE FROM parcel_table WHERE login_id = :id")
    suspend fun deleteParcel(id: String)
}

//Task
@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table")
    fun getAllTask(): LiveData<List<TaskTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(data: List<TaskTable>)

    @Query("DELETE FROM task_table WHERE login_id = :id")
    suspend fun deleteTask(id: String)
}

//Activity
@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity_table")
    fun getAllActivity(): LiveData<List<ActivityTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(data: List<ActivityTable>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateActivities(data: List<ActivityTable>)

    @Query("DELETE FROM activity_table WHERE login_id = :id") //login_id
    suspend fun deleteActivity(id: String)
}

//Fertilizer
@Dao
interface FertilizerDao {
    @Query("SELECT * FROM fertilizer_table")
    fun getAllFertilizer(): LiveData<List<FertilizerTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFertilizer(data: List<FertilizerTable>)

    @Query("DELETE FROM fertilizer_table WHERE login_id = :id") //login_id
    suspend fun deleteFertilizer(id: String)
}


//PestDisease
@Dao
interface PestDiseaseDao {
    @Query("SELECT * FROM pest_disease_table")
    fun getAllPestDisease(): LiveData<List<PestDiseaseTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPestDisease(data: List<PestDiseaseTable>)

    @Query("DELETE FROM pest_disease_table WHERE login_id = :id")
    suspend fun deletePestDisease(id: String)
}

//vehicle
@Dao
interface VehicleDao {
    @Query("SELECT * FROM vehicle_table")
    fun getAllVehicle(): LiveData<List<VehicleTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(data: List<VehicleTable>)

    @Query("DELETE FROM vehicle_table WHERE login_id = :id")
    suspend fun deleteVehicle(id: String)
}

//activityCalender
@Dao
interface ActivityCalenderDao {
    @Query("SELECT * FROM activity_calender_table")
    fun getAllActivityCalender(): LiveData<List<ActivityCalenderTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityCalender(data: List<ActivityCalenderTable>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateActivityCalender(data: List<ActivityCalenderTable>)

    @Query("DELETE FROM activity_calender_table WHERE login_id = :id")
    suspend fun deleteActivityCalender(id: String)
}

//Chemical
@Dao
interface ChemicalDao {
    @Query("SELECT * FROM chemical_table")
    fun getAllChemical(): LiveData<List<ChemicalTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChemical(data: List<ChemicalTable>)

    @Query("DELETE FROM chemical_table WHERE login_id = :id")
    suspend fun deleteChemical(id: String)
}

//driver
@Dao
interface DriverDao {
    @Query("SELECT * FROM driver_table")
    fun getAllDriver(): LiveData<List<DriverTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDriver(data: List<DriverTable>)

    @Query("DELETE FROM driver_table WHERE login_id = :id")
    suspend fun deleteDriver(id: String)
}


@Dao
interface EmpGroupDao {
    @Query("SELECT * FROM emp_group_table")
    fun getAllEmpGroup(): LiveData<List<EmpGroupTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmpGroup(data: List<EmpGroupTable>)

    @Query("DELETE FROM emp_group_table WHERE login_id = :id")
    suspend fun deleteEmpGroup(id: String)
}

@Dao
interface CoreActivityDao {
    @Query("SELECT * FROM core_activity_table")
    fun getAllCoreActivity(): LiveData<List<CoreActivityTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoreActivity(data: List<CoreActivityTable>)

    @Query("DELETE FROM core_activity_table")
    suspend fun deleteCoreActivity()
}



@Dao
interface JobIdListDao {
    @Query("SELECT * FROM Job_ids_table")
    fun getAllJobIdList(): LiveData<List<JobIdList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobIdList(data: JobIdList)

    @Delete
    suspend fun deleteJobIdList(data: JobIdList)
}


//Recode
@Dao
interface HarvestDataDao {
    @Query("SELECT * FROM pro_harvest_data")
    fun getAllHarvest(): LiveData<List<HarvestData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHarvest(data: HarvestData)

    @Delete
    suspend fun deleteHarvest(data: HarvestData)

    @Update
    suspend fun updateHarvest(data: HarvestData)
}

@Dao
interface LooseFruitDataDao {
    @Query("SELECT * FROM pro_loose_fruit_data")
    fun getAllLooseFruit(): LiveData<List<LooseFruitData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLooseFruit(data: LooseFruitData)

    @Delete
    suspend fun deleteLooseFruit(data: LooseFruitData)

    @Update
    suspend fun updateLooseFruit(data: LooseFruitData)
}

@Dao
interface CropLossDataDao {
    @Query("SELECT * FROM pro_crop_loss_data")
    fun getAllCropLoss(): LiveData<List<CropLossData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCropLoss(data: CropLossData)

    @Delete
    suspend fun deleteCropLoss(data: CropLossData) {
        deleteCropLoss(data)
    }

    @Update
    suspend fun updateCropLoss(data: CropLossData)
}

//OtherActivities
@Dao
interface OtherActivityDataDao {
    @Query("SELECT * FROM pro_other_activity_data")
    fun getAllOtherActivity(): LiveData<List<OtherActivityData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOtherActivity(data: OtherActivityData)

    @Delete
    suspend fun deleteOtherActivity(data: OtherActivityData)

    @Update
    suspend fun updateOtherActivity(data: OtherActivityData)
}

//QualityCheckData
@Dao
interface QualityCheckDataDao {
    @Query("SELECT * FROM pro_quality_check_data")
    fun getAllQualityCheck(): LiveData<List<QualityCheckData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQualityCheck(data: QualityCheckData)

    @Delete
    suspend fun deleteQualityCheck(data: QualityCheckData)

    @Update
    suspend fun updateQualityCheck(data: QualityCheckData)
}

//EvacuationData

@Dao
interface EvacuationDataDao {
    @Query("SELECT * FROM pro_evacuation_data")
    fun getAllEvacuation(): LiveData<List<EvacuationData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvacuation(data: EvacuationData)

    @Delete
    suspend fun deleteEvacuation(data: EvacuationData)

    @Update
    suspend fun updateEvacuation(data: EvacuationData)
}

//MaintenanceData

@Dao
interface RingWeedingDataDao {
    @Query("SELECT * FROM mnt_ring_weeding_data")
    fun getAllRingWeeding(): LiveData<List<RingWeedingData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRingWeeding(data: RingWeedingData)

    @Delete
    suspend fun deleteRingWeeding(data: RingWeedingData)

    @Update
    suspend fun updateRingWeeding(data: RingWeedingData)
}


@Dao
interface CircleWeedingDataDao {
    @Query("SELECT * FROM mnt_circle_weeding_data")
    fun getAllCircleWeeding(): LiveData<List<CircleWeedingData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCircleWeeding(data: CircleWeedingData)

    @Delete
    suspend fun deleteCircleWeeding(data: CircleWeedingData)

    @Update
    suspend fun updateCircleWeeding(data: CircleWeedingData)
}

@Dao
interface PruningDataDao {
    @Query("SELECT * FROM mnt_pruning_data")
    fun getAllPruning(): LiveData<List<PruningData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPruning(data: PruningData)

    @Delete
    suspend fun deletePruning(data: PruningData)

    @Update
    suspend fun updatePruning(data: PruningData)
}

@Dao
interface FertilizingDataDao {
    @Query("SELECT * FROM mnt_fertilizing_data")
    fun getAllFertilizing(): LiveData<List<FertilizingData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFertilizing(data: FertilizingData)

    @Delete
    suspend fun deleteFertilizing(data: FertilizingData)

    @Update
    suspend fun updateFertilizing(data: FertilizingData)
}

@Dao
interface RoadMaintenanceDataDao {
    @Query("SELECT * FROM mnt_road_maintenance_data")
    fun getAllRoadMaintenance(): LiveData<List<RoadMaintenanceData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoadMaintenance(data: RoadMaintenanceData)

    @Delete
    suspend fun deleteRoadMaintenance(data: RoadMaintenanceData)

    @Update
    suspend fun updateRoadMaintenance(data: RoadMaintenanceData)
}

@Dao
interface PestDiseasesDataDao {
    @Query("SELECT * FROM mnt_pest_diseases_data")
    fun getAllPestDiseases(): LiveData<List<PestDiseasesData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPestDiseases(data: PestDiseasesData)

    @Delete
    suspend fun deletePestDiseases(data: PestDiseasesData)

    @Update
    suspend fun updatePestDiseases(data: PestDiseasesData)
}

@Dao
interface RingSprayingDataDao {
    @Query("SELECT * FROM mnt_ring_spraying_data")
    fun getAllRingSpraying(): LiveData<List<RingSprayingData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRingSpraying(data: RingSprayingData)

    @Delete
    suspend fun deleteRingSpraying(data: RingSprayingData)

    @Update
    suspend fun updateRingSpraying(data: RingSprayingData)
}

@Dao
interface SelectiveSprayingDataDao {
    @Query("SELECT * FROM mnt_selective_spraying_data")
    fun getAllSelectiveSpraying(): LiveData<List<SelectiveSprayingData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectiveSpraying(data: SelectiveSprayingData)

    @Delete
    suspend fun deleteSelectiveSpraying(data: SelectiveSprayingData)

    @Update
    suspend fun updateSelectiveSpraying(data: SelectiveSprayingData)
}

@Dao
interface CensusDataDao {
    @Query("SELECT * FROM mnt_census_data")
    fun getAllCensus(): LiveData<List<CensusData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCensus(data: CensusData)

    @Delete
    suspend fun deleteCensus(data: CensusData)

    @Update
    suspend fun updateCensus(data: CensusData)
}

@Dao
interface MechWeedingDataDao {
    @Query("SELECT * FROM mnt_mech_weeding_data")
    fun getAllMechWeeding(): LiveData<List<MechWeedingData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMechWeeding(data: MechWeedingData)

    @Delete
    suspend fun deleteMechWeeding(data: MechWeedingData)

    @Update
    suspend fun updateMechWeeding(data: MechWeedingData)
}

@Dao
interface ChemicalWeedingDataDao {
    //type(1-general,2-Ring)
    @Query("SELECT * FROM mnt_chemical_weeding_data WHERE weeding_type = '1'")
    fun getAllChemicalGeneral(): LiveData<List<ChemicalWeedingData>>

    @Query("SELECT * FROM mnt_chemical_weeding_data WHERE weeding_type = '2'")
    fun getAllChemicalRing(): LiveData<List<ChemicalWeedingData>>

    @Query("SELECT * FROM mnt_chemical_weeding_data")
    fun getAllChemical(): LiveData<List<ChemicalWeedingData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChemicalWeed(data: ChemicalWeedingData)

    @Delete
    suspend fun deleteChemicalWeed(data: ChemicalWeedingData)

    @Update
    suspend fun updateChemicalWeed(data: ChemicalWeedingData)
}






