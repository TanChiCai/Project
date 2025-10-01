package com.example.stayeasehotel.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stayeasehotel.ui.uiState.PaymentUiState
import com.example.stayeasehotel.ui.uiState.ReservationUiState
import com.example.stayeasehotel.domain.BookingUseCase
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StaffBookingViewModel(
    private val bookingUseCase: BookingUseCase,
): ViewModel() {
    private val _reservations = MutableStateFlow<List<ReservationUiState>>(emptyList())
    val reservations: StateFlow<List<ReservationUiState>> = _reservations.asStateFlow()
    private val _filterStatus = MutableStateFlow<String?>(null)
    private val _selectedReservation = MutableStateFlow<ReservationUiState?>(null)
    val selectedReservation: StateFlow<ReservationUiState?> = _selectedReservation.asStateFlow()

    private val _payments = MutableStateFlow<List<PaymentUiState>>(emptyList())
    val payments: StateFlow<List<PaymentUiState>> = _payments.asStateFlow()

    private val _selectedPayment = MutableStateFlow<PaymentUiState?>(null)
    val selectedPayment: StateFlow<PaymentUiState?> = _selectedPayment.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toastMessage = _toastMessage.asSharedFlow()

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess.asStateFlow()

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating.asStateFlow()

    private val _removedIds = MutableStateFlow<Set<String>>(emptySet())
    init {
        fetchReservations()
        fetchPayments()
    }

    val filteredReservations: StateFlow<List<ReservationUiState>> =
        combine(_reservations, _filterStatus, _removedIds) { reservations, filterStatus, removed ->
            reservations
                .filter { it.id !in removed } // exclude removed items
                .filter { reservation ->
                    filterStatus?.let { reservation.bookingStatus == it } ?: true
                }
        }.stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            emptyList()
        )

    val filterLabel: StateFlow<String> = _filterStatus
        .map { status -> status ?: "All" }
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            "All"
        )

    fun setFilter(status: String?) {
        _filterStatus.value = status
    }

    fun clearFilter() {
        _filterStatus.value = null
    }

    fun fetchReservations() {
        viewModelScope.launch {
            bookingUseCase.getAllReservations().collect { resList ->
                _reservations.value = resList.map { res ->
                    ReservationUiState(
                        id = res.reservationId,
                        bookingNo = "R-" + res.reservationId.takeLast(4),
                        userName = res.userName,
                        checkInDate = res.checkInDate,
                        checkOutDate = res.checkOutDate,
                        roomId = res.roomId ?: "",
                        roomType = res.roomType ?: "",
                        roomCount = res.roomCount,
                        nights = res.nights ?: 1,
                        totalPrice = res.totalPrice ?: 0.0,
                        requests = res.requests,
                        bookingStatus = res.bookingStatus
                    )
                }
            }
        }

    }

    fun fetchPayments() {
        viewModelScope.launch {
            bookingUseCase.getAllPayments().collect { paylist ->
                _payments.value = paylist.map { pay ->
                    PaymentUiState(
                        id = pay.paymentId,
                        reservationId = pay.reservationId,
                        paymentOption = pay.paymentOption,
                        cardLast4 = pay.cardLast4,
                        amount = pay.amount,
                        paymentStatus = pay.paymentStatus
                    )
                }
            }
        }
    }

    fun getReservationById(reservationId: String) {
        viewModelScope.launch {
            bookingUseCase.getReservationById(reservationId).collect { res ->
                _selectedReservation.value = res?.let {
                    val createdAtTimestamp = it.createdAt?.let { millis ->
                        Timestamp(Date(millis))
                    }

                    ReservationUiState(
                        id = it.reservationId,
                        bookingNo = "R-" + it.reservationId.takeLast(4),
                        userName = it.userName,
                        userEmail = it.userEmail,
                        userPhone = it.userPhone,
                        checkInDate = it.checkInDate,
                        checkOutDate = it.checkOutDate,
                        roomId = it.roomId ?: "",
                        roomType = it.roomType ?: "",
                        roomCount = it.roomCount,
                        nights = it.nights ?: 1,
                        totalPrice = it.totalPrice ?: 0.0,
                        requests = it.requests,
                        bookingStatus = it.bookingStatus,
                        createdAt = createdAtTimestamp
                    )
                }
            }
        }
    }

    fun getPaymentForReservation(reservationId: String) {
        viewModelScope.launch {
            bookingUseCase.getPaymentForReservation(reservationId).collect { pay ->
                _selectedPayment.value = pay?.let {
                    PaymentUiState(
                        id = it.paymentId,
                        reservationId = it.reservationId,
                        paymentOption = it.paymentOption,
                        cardLast4 = it.cardLast4,
                        amount = it.amount,
                        paymentStatus = it.paymentStatus
                    )
                }
            }
        }
    }

    fun updateReservationStatus(reservationId: String, newStatus: String) {
        viewModelScope.launch {
            try {
                bookingUseCase.updateReservationStatus(reservationId, newStatus)
                _toastMessage.emit("Reservation updated successfully!")
            } catch (e: Exception) {
                Log.e("ViewModel", "Offline error: ${e.message}")
                _toastMessage.emit("Please connect to the internet to update reservation.")
            }

        }
    }

    fun resetUpdateSuccess() {
        _updateSuccess.value = false
    }


    fun updatePaymentStatus(paymentId: String, newStatus: String) {
        viewModelScope.launch {
            try {
                bookingUseCase.updatePaymentStatus(paymentId, newStatus)
                _toastMessage.emit("Payment updated successfully!")
            } catch (e: Exception) {
                Log.e("ViewModel", "Offline error: ${e.message}")
                _toastMessage.emit("Please connect to the internet to update payment.")
            }
        }
    }

    suspend fun sendToast(message: String) {
        _toastMessage.emit(message)
    }

    fun removeReservation(reservationId: String) {
        viewModelScope.launch {
            try {
                bookingUseCase.hideReservation(reservationId)
                _toastMessage.emit("Reservation removed")
            } catch (e: Exception) {
                _toastMessage.emit("Failed to remove reservation")
            }
        }
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