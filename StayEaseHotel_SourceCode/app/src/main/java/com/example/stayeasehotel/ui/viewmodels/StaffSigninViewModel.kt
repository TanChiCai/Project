package com.example.stayeasehotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stayeasehotel.data.StaffEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StaffSigninViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StaffEntity())
    val uiState: StateFlow<StaffEntity> = _uiState

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun updateField(field: String, value: String) {
        val currentState = _uiState.value
        _uiState.value = when (field) {
            "name" -> currentState.copy(name = value, errorMessage = null)
            "email" -> currentState.copy(email = value, errorMessage = null)
            "password" -> currentState.copy(password = value, errorMessage = null)
            "areaCode" -> currentState.copy(areaCode = value, errorMessage = null)
            "phoneNumber" -> currentState.copy(phoneNumber = value, errorMessage = null)
            "gender" -> currentState.copy(gender = value, errorMessage = null)
            "position" -> currentState.copy(position = value, errorMessage = null)
            else -> currentState
        }
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$")
        return emailRegex.matches(email)
    }

    private fun getFullPhoneNumber(): String {
        val currentState = _uiState.value
        return if (currentState.areaCode.isNotBlank() && currentState.phoneNumber.isNotBlank()) {
            "${currentState.areaCode}-${currentState.phoneNumber}"
        } else {
            currentState.phoneNum
        }
    }

    private suspend fun checkEmailExists(email: String): Boolean {
        return try {
            // Check in Users collection
            val userQuery = db.collection("Users")
                .whereEqualTo("email", email)
                .get()
                .await()

            // Check in Staff collection
            val staffQuery = db.collection("Staff")
                .whereEqualTo("email", email)
                .get()
                .await()

            !userQuery.isEmpty || !staffQuery.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    fun signUp(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentState = _uiState.value

        // Validation
        if (currentState.name.isBlank() || currentState.email.isBlank() ||
            currentState.password.isBlank() || currentState.areaCode.isBlank() ||
            currentState.phoneNumber.isBlank() || currentState.gender.isBlank() ||
            currentState.position.isBlank()
        ) {
            _uiState.value = currentState.copy(errorMessage = "Please fill in all fields")
            return
        }

        if (!isValidEmail(currentState.email)) {
            _uiState.value = currentState.copy(errorMessage = "Please enter a valid Gmail address")
            return
        }

        if (currentState.password.length < 6) {
            _uiState.value =
                currentState.copy(errorMessage = "Password must be at least 6 characters")
            return
        }

        if (currentState.areaCode.length != 3) {
            _uiState.value = currentState.copy(errorMessage = "Area code must be 3 digits")
            return
        }

        if (currentState.phoneNumber.length < 7 || currentState.phoneNumber.length > 8) {
            _uiState.value = currentState.copy(errorMessage = "Phone number must be 7-8 digits")
            return
        }

        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val emailExists = checkEmailExists(currentState.email)
                if (emailExists) {
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = "Email already exists"
                    )
                    onError("Email already exists")
                    return@launch
                }

                val authResult = auth.createUserWithEmailAndPassword(
                    currentState.email,
                    currentState.password
                ).await()

                val staffId = authResult.user?.uid ?: throw Exception("Staff creation failed")
                val fullPhoneNumber = getFullPhoneNumber()
                val staff = hashMapOf(
                    "staffId" to staffId,
                    "name" to currentState.name,
                    "email" to currentState.email,
                    "areaCode" to currentState.areaCode,
                    "phoneNumber" to currentState.phoneNumber,
                    "phoneNum" to fullPhoneNumber,
                    "gender" to currentState.gender,
                    "position" to currentState.position
                )

                db.collection("Staff").document(staffId).set(staff).await()
                onSuccess()

            } catch (e: Exception) {
                auth.currentUser?.delete()?.await()
                val errorMsg = "Sign up failed: ${e.message}"
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = errorMsg
                )
                onError(errorMsg)
            }
        }
    }
}