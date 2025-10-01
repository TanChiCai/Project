package com.example.stayeasehotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stayeasehotel.ui.uiState.ReservationUiState
import com.example.stayeasehotel.data.repository.StaffReservationRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StaffReservationViewModel(
    private val reservationRepository: StaffReservationRepository
): ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _reservations = MutableStateFlow<List<ReservationUiState>>(emptyList())
    val reservations: StateFlow<List<ReservationUiState>> = _reservations.asStateFlow()
    private val _filterStatus = MutableStateFlow<String?>(null)
    private val _selectedReservation = MutableStateFlow<ReservationUiState?>(null)
    val selectedReservation: StateFlow<ReservationUiState?> = _selectedReservation.asStateFlow()

    init {
        fetchReservations()
    }

    val filteredReservations: StateFlow<List<ReservationUiState>> =
        combine(_reservations, _filterStatus) { reservations, filter ->
            var result: List<ReservationUiState> = reservations

            if (!filter.isNullOrEmpty()) {
                result = reservations.filter { it.bookingStatus == filter }
            }

            result
        }.stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            emptyList()
        )

    fun setFilter(status: String?) {
        _filterStatus.value = status
    }

    fun clearFilter() {
        _filterStatus.value = null
    }

    fun fetchReservations() {
        reservationRepository.getReservationList { resList ->
            _reservations.value = resList
        }
    }

    fun getReservationById(reservationId: String) {
        reservationRepository.getReservationById(reservationId) { reservation ->
            _selectedReservation.value = reservation
        }
    }

    fun updateReservationStatus(reservationId: String, newStatus: String) {
        reservationRepository.updateReservationStatus(reservationId, newStatus)
    }

    fun formatTimestamp(timestamp: Timestamp?): String {
        if (timestamp == null) return "N/A"
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    fun formatLongTimestamp(timestamp: Long?): String {
        if (timestamp == null) return "N/A"
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}