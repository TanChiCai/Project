package com.example.stayeasehotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stayeasehotel.data.UserEntity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {
    private val _navigateToStateChoice = MutableStateFlow(false)
    val navigateToStateChoice: StateFlow<Boolean> = _navigateToStateChoice.asStateFlow()

    private val _userData = MutableStateFlow<UserEntity?>(null)
    val userData: StateFlow<UserEntity?> = _userData.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                clearUserData()
            } else {
                loadUserData()
            }
        }
        loadUserData()
    }

    fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    _loadingState.value = LoadingState.Loading
                    val userDoc = db.collection("Users").document(currentUser.uid).get().await()

                    if (userDoc.exists()) {
                        val user = UserEntity(
                            userId = userDoc.getString("userId") ?: currentUser.uid,
                            name = userDoc.getString("name") ?: currentUser.displayName ?: "",
                            email = userDoc.getString("email") ?: currentUser.email ?: "",
                            password = userDoc.getString("password") ?: "",
                            phoneNum = userDoc.getString("phoneNum") ?: "",
                            gender = userDoc.getString("gender") ?: "",
                            dateOfBirth = userDoc.getString("dateOfBirth") ?: "",
                            confirmPassword = "",
                            areaCode = "",
                            phoneNumber = "",
                            day = "",
                            month = "",
                            year = "",
                            isLoading = false,
                            errorMessage = null,
                            editUserName = userDoc.getString("name") ?: currentUser.displayName ?: "",
                            editUserPhoneNum = userDoc.getString("phoneNum") ?: "",
                            editUserCurrentPassword = "",
                            editUserNewPassword = "",
                            editUserConfirmPassword = "",
                            editUserIsLoading = false,
                            editUserErrorMessage = null,
                            editUserIsSuccess = false
                        )
                        _userData.value = user
                        _loadingState.value = LoadingState.Success(user)
                    } else {
                        _loadingState.value = LoadingState.Error("User data not found")
                    }
                } catch (e: Exception) {
                    _loadingState.value = LoadingState.Error("Failed to load user data: ${e.message}")
                }
            }
        } else {
            _loadingState.value = LoadingState.Error("No user logged in")
        }
    }


    fun updateEditName(name: String) {
        _userData.value = _userData.value?.copy(editUserName = name)
    }

    fun updateEditPhoneNumber(phoneNum: String) {
        _userData.value = _userData.value?.copy(editUserPhoneNum = phoneNum)
    }

    fun updateEditCurrentPassword(currentPassword: String) {
        _userData.value = _userData.value?.copy(editUserCurrentPassword = currentPassword)
    }

    fun updateEditNewPassword(newPassword: String) {
        _userData.value = _userData.value?.copy(editUserNewPassword = newPassword)
    }

    fun updateEditConfirmPassword(confirmPassword: String) {
        _userData.value = _userData.value?.copy(editUserConfirmPassword = confirmPassword)
    }

    fun saveUserChanges() {
        viewModelScope.launch {
            val currentUserData = _userData.value
            val currentAuthUser = auth.currentUser
            val nowPassword = currentUserData?.password

            if (currentUserData == null || currentAuthUser == null) {
                _userData.value = currentUserData?.copy(
                    editUserErrorMessage = "No user logged in",
                    editUserIsLoading = false
                )
                return@launch
            }

            _userData.value = currentUserData.copy(
                editUserIsLoading = true,
                editUserErrorMessage = null
            )

            try {
                if (currentUserData.editUserName.isBlank()) {
                    _userData.value = currentUserData.copy(
                        editUserIsLoading = false,
                        editUserErrorMessage = "Name cannot be empty"
                    )
                    return@launch
                }

                if (currentUserData.editUserPhoneNum != currentUserData.phoneNum &&
                    !currentUserData.editUserPhoneNum.matches(Regex("^[0-9]{3}-[0-9]{7,8}$"))) {
                    _userData.value = currentUserData.copy(
                        editUserIsLoading = false,
                        editUserErrorMessage = "Phone Number must be in format: 000-0000000 or 000-00000000"
                    )
                    return@launch
                }

                val isChangingPassword = currentUserData.editUserCurrentPassword.isNotBlank() ||
                        currentUserData.editUserNewPassword.isNotBlank() ||
                        currentUserData.editUserConfirmPassword.isNotBlank()

                if (isChangingPassword) {
                    if (currentUserData.editUserCurrentPassword != currentUserData.password) {
                        _userData.value = currentUserData.copy(
                            editUserIsLoading = false,
                            editUserErrorMessage = "Current password is not your original password"
                        )
                        return@launch
                    }

                    if (currentUserData.editUserNewPassword != currentUserData.editUserConfirmPassword) {
                        _userData.value = currentUserData.copy(
                            editUserIsLoading = false,
                            editUserErrorMessage = "New passwords do not match"
                        )
                        return@launch
                    }

                    if (currentUserData.editUserNewPassword.length < 6) {
                        _userData.value = currentUserData.copy(
                            editUserIsLoading = false,
                            editUserErrorMessage = "Password must be at least 6 characters"
                        )
                        return@launch
                    }

                    val credential = EmailAuthProvider.getCredential(
                        currentAuthUser.email!!,
                        currentUserData.editUserCurrentPassword
                    )
                    currentAuthUser.reauthenticate(credential).await()

                    currentAuthUser.updatePassword(currentUserData.editUserNewPassword).await()
                }
                if (currentUserData.editUserNewPassword.isBlank()) {
                    currentUserData.editUserNewPassword = nowPassword.toString()
                }

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(currentUserData.editUserName)
                    .build()
                currentAuthUser.updateProfile(profileUpdates).await()

                val userUpdates = hashMapOf<String, Any>(
                    "name" to currentUserData.editUserName,
                    "phoneNum" to currentUserData.editUserPhoneNum,
                    "password" to currentUserData.editUserNewPassword
                )

                db.collection("Users").document(currentAuthUser.uid)
                    .update(userUpdates as Map<String, Any>)
                    .await()

                _userData.value = currentUserData.copy(
                    name = currentUserData.editUserName,
                    phoneNum = currentUserData.editUserPhoneNum,
                    password = currentUserData.editUserNewPassword,
                    editUserIsLoading = false,
                    editUserIsSuccess = true,
                    editUserErrorMessage = null,
                    editUserCurrentPassword = "",
                    editUserNewPassword = "",
                    editUserConfirmPassword = ""
                )

            } catch (e: Exception) {
                _userData.value = currentUserData.copy(
                    editUserIsLoading = false,
                    editUserErrorMessage = when {
                        e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ->
                            "Current password is incorrect"
                        e.message?.contains("requires recent authentication") == true ->
                            "Please re-authenticate to change password"
                        else -> "Failed to update profile: ${e.message}"
                    }
                )

            }
        }
    }

    fun clearEditError() {
        _userData.value = _userData.value?.copy(editUserErrorMessage = null)
    }

    fun resetEditSuccess() {
        _userData.value = _userData.value?.copy(editUserIsSuccess = false)
    }

    fun triggerStateChoiceNavigation() {
        viewModelScope.launch {
            _navigateToStateChoice.value = true
        }
    }

    fun resetNavigation() {
        viewModelScope.launch {
            _navigateToStateChoice.value = false
        }
    }

    fun clearUserData() {
        _userData.value = null
        _loadingState.value = LoadingState.Loading
    }

    sealed class LoadingState {
        object Loading : LoadingState()
        data class Success(val user: UserEntity) : LoadingState()
        data class Error(val message: String) : LoadingState()
    }
}