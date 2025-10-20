package com.airei.app.phc.attendance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airei.app.phc.attendance.entity.BlockEntity
import com.airei.app.phc.attendance.entity.DivisionEntity
import com.airei.app.phc.attendance.entity.EstateEntity
import com.airei.app.phc.attendance.entity.ParcelEntity
import com.airei.app.phc.attendance.room.repo.RoomDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MasterDataViewModel @Inject constructor(
    private val repository: RoomDbRepository
) : ViewModel() {

    // 🏠 Estate
    fun insertEstates(list: List<EstateEntity>) = viewModelScope.launch {
        repository.insertEstateList(list)
    }

    fun deleteAllEstates() = viewModelScope.launch {
        repository.deleteAllEstates()
    }

    fun getAllEstates(): LiveData<List<EstateEntity>> = repository.getAllEstates()

    // 🏢 Division
    fun insertDivisions(list: List<DivisionEntity>) = viewModelScope.launch {
        repository.insertDivisionList(list)
    }

    fun deleteAllDivisions() = viewModelScope.launch {
        repository.deleteAllDivisions()
    }

    fun getAllDivisions(): LiveData<List<DivisionEntity>> = repository.getAllDivisions()

    // 🌳 Block
    fun insertBlocks(list: List<BlockEntity>) = viewModelScope.launch {
        repository.insertBlockList(list)
    }

    fun deleteAllBlocks() = viewModelScope.launch {
        repository.deleteAllBlocks()
    }

    fun getAllBlocks(): LiveData<List<BlockEntity>> = repository.getAllBlocks()

    // 📦 Parcel
    fun insertParcels(list: List<ParcelEntity>) = viewModelScope.launch {
        repository.insertParcelList(list)
    }

    fun deleteAllParcels() = viewModelScope.launch {
        repository.deleteAllParcels()
    }

    fun getAllParcels(): LiveData<List<ParcelEntity>> = repository.getAllParcels()
}

