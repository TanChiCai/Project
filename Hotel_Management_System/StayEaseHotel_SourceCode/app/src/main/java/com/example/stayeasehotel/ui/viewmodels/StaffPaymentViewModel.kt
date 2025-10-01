package com.example.stayeasehotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.stayeasehotel.ui.uiState.PaymentUiState
import com.example.stayeasehotel.data.repository.StaffPaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StaffPaymentViewModel(
    private val paymentRepository: StaffPaymentRepository
) : ViewModel() {
    private val _payments = MutableStateFlow<List<PaymentUiState>>(emptyList())

    private val _selectedPayment = MutableStateFlow<PaymentUiState?>(null)
    val selectedPayment: StateFlow<PaymentUiState?> = _selectedPayment.asStateFlow()

    init {
        fetchPayments()
    }
    fun fetchPayments() {
        paymentRepository.getPaymentLists { payList ->
            _payments.value = payList
        }
    }

    fun getPaymentById(reservationId: String) {
        paymentRepository.getPaymentByReservationId(reservationId) { payment ->
            _selectedPayment.value = payment
        }
    }

    fun updatePaymentStatus(paymentId: String, newStatus: String) {
        paymentRepository.updatePaymentStatus(paymentId, newStatus)
    }
}