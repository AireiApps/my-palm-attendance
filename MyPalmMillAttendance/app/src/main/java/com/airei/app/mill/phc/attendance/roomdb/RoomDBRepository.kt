package com.airei.app.plantation.phc.roomdb

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
import javax.inject.Inject

class RoomDBRepository @Inject constructor(
    private val loginUserDao: LoginUserDao,
    private val empTableDao: EmpTableDao,
    private val employeeBioDao: EmployeeBioDao,
    private val empAttendanceDao: EmpAttendanceDao,
    private val estateDao: EstateDao,
    private val divisionDao: DivisionDao,
    private val blockDao: BlockDao,
    private val parcelDao: ParcelDao,
    private val taskDao: TaskDao,
    private val empGroupDao: EmpGroupDao,
    private val activityDao: ActivityDao,
    private val fertilizerDao: FertilizerDao,
    private val pestDiseaseDao: PestDiseaseDao,
    private val vehicleDao: VehicleDao,
    private val activityCalenderDao: ActivityCalenderDao,
    private val chemicalDao: ChemicalDao,
    private val driverDao: DriverDao,
    private val coreActivityDao: CoreActivityDao,


    private val jobIdListDao: JobIdListDao,
//prodection
    private val harvestDataDao: HarvestDataDao,
    private val looseFruitDataDao: LooseFruitDataDao,
    private val qualityCheckDataDao: QualityCheckDataDao,
    private val cropLossDataDao: CropLossDataDao,
    private val evacuationDataDao: EvacuationDataDao,
    private val otherActivityDataDao: OtherActivityDataDao,

    //Maintenance
    private val ringWeedingDataDao: RingWeedingDataDao,
    private val circleWeedingDataDao: CircleWeedingDataDao,
    private val pruningDataDao: PruningDataDao,
    private val fertilizingDataDao: FertilizingDataDao,
    private val roadMaintenanceDataDao: RoadMaintenanceDataDao,
    private val pestDiseasesDataDao: PestDiseasesDataDao,
    private val ringSprayingDataDao: RingSprayingDataDao,
    private val selectiveSprayingDataDao: SelectiveSprayingDataDao,
    private val censusDataDao: CensusDataDao,
    private val mechWeedingDataDao: MechWeedingDataDao,
    private val chemicalWeedingDataDao: ChemicalWeedingDataDao,

) {

    //Login User
    suspend fun insertLoginUser(data: LoginUserTable) {
        loginUserDao.insertLoginUser(data)
    }

    suspend fun updateLoginUser(data: LoginUserTable) {
        loginUserDao.updateLoginUser(data)
    }

    suspend fun deleteLoginUser(data: LoginUserTable) {
        loginUserDao.deleteLoginUser(data)
    }

    fun getLoginUser() = loginUserDao.getAllLoginUser()

    // EmpTableDao

    suspend fun insertEmpTable(data: EmployeeTable) {
        empTableDao.insertEmpTable(data)
    }

    suspend fun insertEmpTableList(data: List<EmployeeTable>) {
        empTableDao.insertEmpTableList(data)
    }

    suspend fun updateEmpTable(data: EmployeeTable) {
        empTableDao.updateEmpTable(data)
    }

    suspend fun deleteEmpTable(data: EmployeeTable) {
        empTableDao.deleteEmpTable(data)
    }

    suspend fun deleteAllEmpTable() = empTableDao.deleteAllEmpTable()

    fun getAllEmpTable() = empTableDao.getAllEmpTable()

    // EmployeeBioDao
    fun getAllEmployeeBio() = employeeBioDao.getAllEmployeeBio()

    suspend fun insertEmpBio(data: EmployeeBioTable) {
        employeeBioDao.insertEmpBio(data)
    }

    suspend fun insertEmpBioList(data: List<EmployeeBioTable>) {
        employeeBioDao.insertEmpBioList(data)
    }

    suspend fun deleteEmpBio(data: EmployeeBioTable) {
        employeeBioDao.deleteEmpBio(data)
    }

    suspend fun updateEmpBio(data: EmployeeBioTable) {
        employeeBioDao.updateEmpBio(data)
    }

    suspend fun deleteAllEmpBio() = employeeBioDao.deleteAllEmpBio()

    // EmpAttendanceDao
    fun getAllEmpAttendance() = empAttendanceDao.getAllEmpAttendance()
    suspend fun insertEmpAttendance(data: EmpAttendanceTable) {
        empAttendanceDao.insertEmpAttendance(data)
    }

    suspend fun deleteEmpAttendance(data: EmpAttendanceTable) {
        empAttendanceDao.deleteEmpAttendance(data)
    }

    suspend fun updateEmpAttendance(data: EmpAttendanceTable) {
        empAttendanceDao.updateEmpAttendance(data)
    }

    // EstateDao
    fun getAllEstates() = estateDao.getAllEstates()
    suspend fun insertEstates(data: List<EstateTable>) {
        estateDao.insertEstates(data)
    }

    suspend fun deleteEstates(id: String) {
        estateDao.deleteEstates(id)
    }

    // DivisionDao
    fun getAllDivision() = divisionDao.getAllDivision()
    suspend fun insertDivisions(data: List<DivisionTable>) {
        divisionDao.insertDivisions(data)
    }

    suspend fun deleteDivisions(id: String) {
        divisionDao.deleteDivisions(id)
    }

    // BlockDao
    fun getAllBlock() = blockDao.getAllBlock()
    suspend fun insertBlocks(data: List<BlockTable>) {
        blockDao.insertBlocks(data)
    }

    suspend fun deleteBlocks(id: String) {
        blockDao.deleteBlocks(id)
    }

    // ParcelDao
    fun getAllParcel() = parcelDao.getAllParcel()
    suspend fun insertParcel(data: List<ParcelTable>) {
        parcelDao.insertParcel(data)
    }

    suspend fun deleteParcel(id: String) {
        parcelDao.deleteParcel(id)
    }

    // TaskDao
    fun getAllTask() = taskDao.getAllTask()
    suspend fun insertTask(data: List<TaskTable>) {
        taskDao.insertTask(data)
    }

    suspend fun deleteTask(id: String) {
        taskDao.deleteTask(id)
    }

    // ActivityDao
    fun getAllActivity() = activityDao.getAllActivity()
    suspend fun insertActivity(data: List<ActivityTable>) {
        activityDao.insertActivities(data)
    }
    suspend fun updateActivities(data: List<ActivityTable>) {
        activityDao.updateActivities(data)
    }
    suspend fun deleteActivity(id: String) {
        activityDao.deleteActivity(id)
    }

    // EmpGroupDao
    fun getAllEmpGroup() = empGroupDao.getAllEmpGroup()
    suspend fun insertEmpGroup(data: List<EmpGroupTable>) {
        empGroupDao.insertEmpGroup(data)
    }

    suspend fun deleteEmpGroup(id: String) {
        empGroupDao.deleteEmpGroup(id)
    }

    // FertilizerDao
    fun getAllFertilizer() = fertilizerDao.getAllFertilizer()
    suspend fun insertFertilizer(data: List<FertilizerTable>) {
        fertilizerDao.insertFertilizer(data)
    }

    suspend fun deleteFertilizer(id: String) {
        fertilizerDao.deleteFertilizer(id)
    }

    // PestDiseaseDao
    fun getAllPestDisease() = pestDiseaseDao.getAllPestDisease()
    suspend fun insertPestDisease(data: List<PestDiseaseTable>) {
        pestDiseaseDao.insertPestDisease(data)
    }

    suspend fun deletePestDisease(id: String) {
        pestDiseaseDao.deletePestDisease(id)
    }

    // VehicleDao
    fun getAllVehicle() = vehicleDao.getAllVehicle()
    suspend fun insertVehicle(data: List<VehicleTable>) {
        vehicleDao.insertVehicle(data)
    }

    suspend fun deleteVehicle(id: String) {
        vehicleDao.deleteVehicle(id)
    }

    // ActivityCalenderDao
    fun getAllActivityCalender() = activityCalenderDao.getAllActivityCalender()
    suspend fun insertActivityCalender(data: List<ActivityCalenderTable>) {
        activityCalenderDao.insertActivityCalender(data)
    }
    suspend fun updateActivityCalender(data: List<ActivityCalenderTable>) {
        activityCalenderDao.updateActivityCalender(data)
    }

    suspend fun deleteActivityCalender(id: String) {
        activityCalenderDao.deleteActivityCalender(id)
    }

    // ChemicalDao
    fun getAllChemical() = chemicalDao.getAllChemical()
    suspend fun insertChemical(data: List<ChemicalTable>) {
        chemicalDao.insertChemical(data)
    }

    suspend fun deleteChemical(id: String) {
        chemicalDao.deleteChemical(id)
    }

    // DriverDao
    fun getAllDriver() = driverDao.getAllDriver()
    suspend fun insertDriver(data: List<DriverTable>) {
        driverDao.insertDriver(data)
    }

    suspend fun deleteDriver(id: String) {
        driverDao.deleteDriver(id)
    }

    // CoreActivityDao
    fun getAllCoreActivity() = coreActivityDao.getAllCoreActivity()
    suspend fun insertCoreActivity(data: List<CoreActivityTable>) {
        coreActivityDao.insertCoreActivity(data)
    }
    suspend fun deleteCoreActivity() {
        coreActivityDao.deleteCoreActivity()
    }


    // JobIdListDao
    fun getAllJobIdList() = jobIdListDao.getAllJobIdList()
    suspend fun insertJobIdList(data: JobIdList) {
        jobIdListDao.insertJobIdList(data)
    }

    suspend fun deleteJobIdList(data: JobIdList) {
        jobIdListDao.deleteJobIdList(data)
    }

    // Record
    // Harvesting
    fun getAllHarvest() = harvestDataDao.getAllHarvest()

    suspend fun insertHarvest(data: HarvestData) {
        harvestDataDao.insertHarvest(data)
    }

    suspend fun deleteHarvest(data: HarvestData) {
        harvestDataDao.deleteHarvest(data)
    }

    suspend fun updateHarvest(data: HarvestData) {
        harvestDataDao.updateHarvest(data)
    }

    // Loose Fruit
    fun getAllLooseFruit() = looseFruitDataDao.getAllLooseFruit()

    suspend fun insertLooseFruit(data: LooseFruitData) {
        looseFruitDataDao.insertLooseFruit(data)
    }

    suspend fun deleteLooseFruit(data: LooseFruitData) {
        looseFruitDataDao.deleteLooseFruit(data)
    }

    suspend fun updateLooseFruit(data: LooseFruitData) {
        looseFruitDataDao.updateLooseFruit(data)
    }

    //qualityCheckDataDao
    fun getAllQualityCheck() = qualityCheckDataDao.getAllQualityCheck()

    suspend fun insertQualityCheck(data: QualityCheckData) {
        qualityCheckDataDao.insertQualityCheck(data)
    }

    suspend fun deleteQualityCheck(data: QualityCheckData) {
        qualityCheckDataDao.deleteQualityCheck(data)
    }

    suspend fun updateQualityCheck(data: QualityCheckData) {
        qualityCheckDataDao.updateQualityCheck(data)
    }

    //cropLossDataDao
    fun getAllCropLoss() = cropLossDataDao.getAllCropLoss()

    suspend fun insertCropLoss(data: CropLossData) {
        cropLossDataDao.insertCropLoss(data)
    }

    suspend fun deleteCropLoss(data: CropLossData) {
        cropLossDataDao.deleteCropLoss(data)
    }

    suspend fun updateCropLoss(data: CropLossData) {
        cropLossDataDao.updateCropLoss(data)
    }

    //evacuationDataDao
    fun getAllEvacuation() = evacuationDataDao.getAllEvacuation()

    suspend fun insertEvacuation(data: EvacuationData) {
        evacuationDataDao.insertEvacuation(data)
    }

    suspend fun deleteEvacuation(data: EvacuationData) {
        evacuationDataDao.deleteEvacuation(data)
    }

    suspend fun updateEvacuation(data: EvacuationData) {
        evacuationDataDao.updateEvacuation(data)
    }

    //otherActivityDataDao
    fun getAllOtherActivity() = otherActivityDataDao.getAllOtherActivity()

    suspend fun insertOtherActivity(data: OtherActivityData) {
        otherActivityDataDao.insertOtherActivity(data)
    }

    suspend fun deleteOtherActivity(data: OtherActivityData) {
        otherActivityDataDao.deleteOtherActivity(data)
    }

    suspend fun updateOtherActivity(data: OtherActivityData) {
        otherActivityDataDao.updateOtherActivity(data)
    }

    //mainActivityDataDao

    //ringWeedingDataDao
    fun getAllRingWeeding() = ringWeedingDataDao.getAllRingWeeding()

    suspend fun insertRingWeeding(data: RingWeedingData) {
        ringWeedingDataDao.insertRingWeeding(data)
    }

    suspend fun deleteRingWeeding(data: RingWeedingData) {
        ringWeedingDataDao.deleteRingWeeding(data)
    }

    suspend fun updateRingWeeding(data: RingWeedingData) {
        ringWeedingDataDao.updateRingWeeding(data)
    }

    //circleWeedingDataDao
    fun getAllCircleWeeding() = circleWeedingDataDao.getAllCircleWeeding()

    suspend fun insertCircleWeeding(data: CircleWeedingData) {
        circleWeedingDataDao.insertCircleWeeding(data)
    }

    suspend fun deleteCircleWeeding(data: CircleWeedingData) {
        circleWeedingDataDao.deleteCircleWeeding(data)
    }

    suspend fun updateCircleWeeding(data: CircleWeedingData) {
        circleWeedingDataDao.updateCircleWeeding(data)
    }

    //pruningDataDao
    fun getAllPruning() = pruningDataDao.getAllPruning()

    suspend fun insertPruning(data: PruningData) {
        pruningDataDao.insertPruning(data)
    }

    suspend fun deletePruning(data: PruningData) {
        pruningDataDao.deletePruning(data)
    }

    suspend fun updatePruning(data: PruningData) {
        pruningDataDao.updatePruning(data)
    }

    //fertilizingDataDao
    fun getAllFertilizing() = fertilizingDataDao.getAllFertilizing()

    suspend fun insertFertilizing(data: FertilizingData) {
        fertilizingDataDao.insertFertilizing(data)
    }

    suspend fun deleteFertilizing(data: FertilizingData) {
        fertilizingDataDao.deleteFertilizing(data)
    }

    suspend fun updateFertilizing(data: FertilizingData) {
        fertilizingDataDao.updateFertilizing(data)
    }

    //roadMaintenanceDataDao
    fun getAllRoadMaintenance() = roadMaintenanceDataDao.getAllRoadMaintenance()

    suspend fun insertRoadMaintenance(data: RoadMaintenanceData) {
        roadMaintenanceDataDao.insertRoadMaintenance(data)
    }

    suspend fun deleteRoadMaintenance(data: RoadMaintenanceData) {
        roadMaintenanceDataDao.deleteRoadMaintenance(data)
    }

    suspend fun updateRoadMaintenance(data: RoadMaintenanceData) {
        roadMaintenanceDataDao.updateRoadMaintenance(data)
    }

    //pestDiseasesDataDao
    fun getAllPestDiseases() = pestDiseasesDataDao.getAllPestDiseases()

    suspend fun insertPestDiseases(data: PestDiseasesData) {
        pestDiseasesDataDao.insertPestDiseases(data)
    }

    suspend fun deletePestDiseases(data: PestDiseasesData) {
        pestDiseasesDataDao.deletePestDiseases(data)
    }

    suspend fun updatePestDiseases(data: PestDiseasesData) {
        pestDiseasesDataDao.updatePestDiseases(data)
    }

    //ringSprayingDataDao
    fun getAllRingSpraying() = ringSprayingDataDao.getAllRingSpraying()

    suspend fun insertRingSpraying(data: RingSprayingData) {
        ringSprayingDataDao.insertRingSpraying(data)
    }

    suspend fun deleteRingSpraying(data: RingSprayingData) {
        ringSprayingDataDao.deleteRingSpraying(data)
    }

    suspend fun updateRingSpraying(data: RingSprayingData) {
        ringSprayingDataDao.updateRingSpraying(data)
    }

    //selectiveSprayingDataDao
    fun getAllSelectiveSpraying() = selectiveSprayingDataDao.getAllSelectiveSpraying()

    suspend fun insertSelectiveSpraying(data: SelectiveSprayingData) {
        selectiveSprayingDataDao.insertSelectiveSpraying(data)
    }

    suspend fun deleteSelectiveSpraying(data: SelectiveSprayingData) {
        selectiveSprayingDataDao.deleteSelectiveSpraying(data)
    }

    suspend fun updateSelectiveSpraying(data: SelectiveSprayingData) {
        selectiveSprayingDataDao.updateSelectiveSpraying(data)
    }

    //censusDataDao
    fun getAllCensus() = censusDataDao.getAllCensus()

    suspend fun insertCensus(data: CensusData) {
        censusDataDao.insertCensus(data)
    }

    suspend fun deleteCensus(data: CensusData) {
        censusDataDao.deleteCensus(data)
    }

    suspend fun updateCensus(data: CensusData) {
        censusDataDao.updateCensus(data)
    }

    //mechWeedingDataDao
    fun getAllMechWeeding() = mechWeedingDataDao.getAllMechWeeding()

    suspend fun insertMechWeeding(data: MechWeedingData) {
        mechWeedingDataDao.insertMechWeeding(data)
    }

    suspend fun deleteMechWeeding(data: MechWeedingData) {
        mechWeedingDataDao.deleteMechWeeding(data)
    }

    suspend fun updateMechWeeding(data: MechWeedingData) {
        mechWeedingDataDao.updateMechWeeding(data)
    }

    //chemicalWeedingDataDao
    fun getAllChemicalGeneral() = chemicalWeedingDataDao.getAllChemicalGeneral()

    fun getAllChemicalRing() = chemicalWeedingDataDao.getAllChemicalRing()

    fun getAllChemicalWeeding() = chemicalWeedingDataDao.getAllChemical()

    suspend fun insertChemicalWeed(data: ChemicalWeedingData) {
        chemicalWeedingDataDao.insertChemicalWeed(data)
    }

    suspend fun deleteChemicalWeed(data: ChemicalWeedingData) {
        chemicalWeedingDataDao.deleteChemicalWeed(data)
    }

    suspend fun updateChemicalWeed(data: ChemicalWeedingData) {
        chemicalWeedingDataDao.updateChemicalWeed(data)
    }
}