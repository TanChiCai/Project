package com.example.stayeasehotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stayeasehotel.data.UserEntity
import com.example.stayeasehotel.service.AuthService
import com.example.stayeasehotel.ui.StaffSession
import com.example.stayeasehotel.ui.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserLoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UserEntity())
    val uiState: StateFlow<UserEntity> = _uiState.asStateFlow()

    private val _userData = MutableStateFlow(UserEntity())
    val userData: StateFlow<UserEntity> = _userData.asStateFlow()

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private suspend fun getUserType(userId: String): String {
        return try {
            // Check if user exists in Users collection
            val userDoc = db.collection("Users").document(userId).get().await()
            if (userDoc.exists()) {
                "user"
            } else {
                // Check if user exists in Staff collection
                val staffDoc = db.collection("Staff").document(userId).get().await()
                if (staffDoc.exists()) {
                    "staff"
                } else {
                    "unknown"
                }
            }
        } catch (e: Exception) {
            "unknown"
        }
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun login(
        onSuccess: (UserEntity) -> Unit,
        onError: (String) -> Unit
    ) {
        val currentState = _uiState.value

        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Please fill in all fields")
            return
        }

        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                // Direct Firebase authentication
                val authResult = auth.signInWithEmailAndPassword(
                    currentState.email,
                    currentState.password
                ).await()

                val userId = authResult.user?.uid ?: throw Exception("Login failed")

                // Check user type directly in Firestore
                val userType = getUserType(userId)

                if (userType == "user") {
                    val userData = getUserData(userId)
                    val user = AuthService.getUserDetails(userId)
                    if (userData != null) {
                        StaffSession.currentStaff = null



                        UserSession.currentUser = user
                        _userData.value = userData
                        onSuccess(userData)
                    } else {
                        throw Exception("User data not found")
                    }
                } else {
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = "This is not a user account"
                    )
                    onError("This is not a user account")
                }
            } catch (e: Exception) {
                val errorMsg = "Login failed: ${e.message}"
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = errorMsg
                )
                onError(errorMsg)
            }
        }
    }


    private suspend fun getUserData(userId: String): UserEntity? {
        return try {
            // Try to get user data from Users collection
            val userDoc = db.collection("Users").document(userId).get().await()
            if (userDoc.exists()) {
                UserEntity(
                    userId = userId,
                    name = userDoc.getString("name") ?: "",
                    email = userDoc.getString("email") ?: "",
                    password = "", // Don't retrieve password for security
                    phoneNum = userDoc.getString("phoneNum") ?: "",
                    gender = userDoc.getString("gender") ?: "",
                    dateOfBirth = userDoc.getString("dateOfBirth") ?: "",
                )
            } else {
                // Try to get user data from Staff collection
                val staffDoc = db.collection("Staff").document(userId).get().await()
                if (staffDoc.exists()) {
                    UserEntity(
                        userId = userId,
                        name = staffDoc.getString("name") ?: "",
                        email = staffDoc.getString("email") ?: "",
                        password = "", // Don't retrieve password for security
                        phoneNum = staffDoc.getString("phoneNum") ?: "",
                        gender = staffDoc.getString("gender") ?: "",
                        dateOfBirth = staffDoc.getString("dateOfBirth") ?: "",
                        // Other fields can be set to default or retrieved if needed
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}