package com.example.stayeasehotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stayeasehotel.data.StaffEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StaffManagementViewModel : ViewModel() {
    private val _staffList = MutableStateFlow<List<StaffEntity>>(emptyList())
    val staffList: StateFlow<List<StaffEntity>> = _staffList.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _currentStaffPosition = MutableStateFlow<String?>(null)
    val currentStaffPosition: StateFlow<String?> = _currentStaffPosition.asStateFlow()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun loadStaff() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val snapshot = db.collection("Staff").get().await()
                val staffMembers = mutableListOf<StaffEntity>()

                for (document in snapshot.documents) {
                    val staff = StaffEntity(
                        staffId = document.id,
                        name = document.getString("name") ?: "",
                        email = document.getString("email") ?: "",
                        phoneNum = document.getString("phoneNum") ?: "",
                        gender = document.getString("gender") ?: "",
                        position = document.getString("position") ?: ""
                    )
                    staffMembers.add(staff)
                }

                _staffList.value = staffMembers
                loadCurrentStaffPosition()

            } catch (e: Exception) {
                _error.value = "Failed to load staff: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    private suspend fun loadCurrentStaffPosition() {
        val currentStaff = auth.currentUser
        if (currentStaff != null) {
            try {
                val staffDoc = db.collection("Staff").document(currentStaff.uid).get().await()
                if (staffDoc.exists()) {
                    _currentStaffPosition.value = staffDoc.getString("position") ?: ""
                }
            } catch (e: Exception) {}
        }
    }

    fun updateStaffPosition(staffId: String, newPosition: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                db.collection("Staff").document(staffId)
                    .update("position", newPosition)
                    .await()
                loadStaff()
                onSuccess()

            } catch (e: Exception) {
                onError("Failed to update staff position: ${e.message}")
            }
        }
    }

    fun deleteStaff(staffId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                db.collection("Staff").document(staffId).delete().await()
                loadStaff()
                onSuccess()

            } catch (e: Exception) {
                onError("Failed to delete staff: ${e.message}")
            }
        }
    }
}