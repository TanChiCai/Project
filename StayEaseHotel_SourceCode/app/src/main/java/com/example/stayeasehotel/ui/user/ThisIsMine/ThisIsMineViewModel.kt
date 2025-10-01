package com.example.stayeasehotel.ui.user.ThisIsMine

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.stayeasehotel.data.ClaimData.ClaimEntity
import com.example.stayeasehotel.data.ClaimData.ClaimItemDatabase
import com.example.stayeasehotel.data.ClaimData.ClaimRepository
import com.example.stayeasehotel.data.LostItemData.LostItemEntity

import com.example.stayeasehotel.ui.user.ThisIsMine.ThisIsMineUiState
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.core.net.toUri
import com.example.stayeasehotel.data.LostItemData.HotelLostItemDatabase
import com.example.stayeasehotel.data.LostItemData.LostItemRepository
import com.example.stayeasehotel.ui.UserSession
import java.io.File


class ThisIsMineViewModel(application: Application) : AndroidViewModel(application) {


    private val _uiState = MutableStateFlow(ThisIsMineUiState())
    val uiState: StateFlow<ThisIsMineUiState> = _uiState.asStateFlow()
    private val claimRepository: ClaimRepository
    private val lostRepository: LostItemRepository
    private val reporter =UserSession.currentUser



    fun loadItemById(itemId: String) {
        viewModelScope.launch {
            val item = lostRepository.getItemById(itemId)
            if (item != null) {
                _uiState.update { it.copy(loadedItem = item) }
            } else {
                _uiState.update { it.copy(errorMessage = "Item not found.") }
            }
        }
    }

    fun setDesription(description: String) {
        _uiState.value = _uiState.value.copy(claimDescription = description)
    }

    fun setSelectedImages(uris: List<Uri>) {
        _uiState.value = _uiState.value.copy(selectedImages = _uiState.value.selectedImages + uris)
    }

    fun setMark(mark: String) {
        _uiState.value = _uiState.value.copy(marks = mark)
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, submissionSuccess = false)
    }


    fun setNote(note: String) {
        _uiState.value = _uiState.value.copy(notes = note)
    }




    init {
        val claimItemDao = ClaimItemDatabase.getClaimItemDatabase(application).claimItemDao()
        claimRepository = ClaimRepository(claimItemDao)
        val lostItemDao = HotelLostItemDatabase.getLostItemDatabase(application).lostItemDao()
        lostRepository = LostItemRepository(lostItemDao)

        viewModelScope.launch {
            claimRepository.syncFromFirestore()



        }
    }

    fun update(claimEntity: ClaimEntity) = viewModelScope.launch {
        claimRepository.update(claimEntity)
    }

    fun delete(claimEntity: ClaimEntity) = viewModelScope.launch {
        claimRepository.delete(claimEntity)
    }

    fun createImageUri(context: Context): Uri {
        val imageFile = File(context.cacheDir, "captured_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
        Log.d("CameraDebug", "Generated URI: $uri")
        return uri
    }

    fun setShowDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showDialog = show)
    }

    fun setPreviewIndexChange(index: Int) {
        _uiState.value = _uiState.value.copy(previewIndex = index)
    }



    fun setCameraImageUri(uri: Uri) {
        _uiState.value = _uiState.value.copy(cameraImageUri = uri)

    }
    fun addCapturedImage(uri: Uri?) {
        uri?.let { nonNullUri ->
            val updatedList = _uiState.value.selectedImages.toMutableList()
            updatedList.add(nonNullUri)
            _uiState.value = _uiState.value.copy(selectedImages = updatedList)
        }
    }

    fun removeSelectedImage(index: Int) {
        val updatedList = _uiState.value.selectedImages.toMutableList()
        if (index in updatedList.indices) {
            updatedList.removeAt(index)
            _uiState.value = _uiState.value.copy(selectedImages = updatedList)
        }
    }

    fun submitClaim() {
        val state = _uiState.value





        _uiState.value = state.copy(isSubmitting = true)
        val requiredFieldsFilled = state.claimDescription.isNotBlank()
                && state.marks.isNotBlank()




        viewModelScope.launch {

            val newId = claimRepository.generateSequentialIdFromFireBase("C")

            _uiState.update { currentState ->
                currentState.copy(claimId = newId)
            }
            _uiState.update { currentState ->
                currentState.copy(showSuccessDialog = true)
            }
            _uiState.update { currentState ->
                currentState.copy(itemStatus  = "pending")
            }
            if (requiredFieldsFilled) {

                try {


                    val loadedItem = _uiState.value.loadedItem



                    val newClaimItem = ClaimEntity(
                        claimId = newId,
                        claimDescription = state.claimDescription,
                        marks = state.marks,
                        proofFileUri = state.selectedImages.map { it.toString() }, // convert URI list to String
                        notes = state.notes,
                        claimTime = System.currentTimeMillis(),
                        claimStatus = "Pending",
                        item = loadedItem,
                        claimer = reporter
                    )





                    claimRepository.insert(newClaimItem)

                    _uiState.value = state.copy(
                        claimDescription = "",
                        marks = "",
                        notes ="",
                        claimTime = System.currentTimeMillis(),
                        selectedImages=emptyList(),

                        isSubmitting = false,
                        submissionSuccess = true,
                        errorMessage = null,
                        claimId = newId
                    )



                } catch (e: Exception) {
                    _uiState.value = state.copy(
                        isSubmitting = false,
                        submissionSuccess = false,
                        errorMessage = e.message
                    )
                }
            } else {
                _uiState.value = state.copy(
                    isSubmitting = false,
                    submissionSuccess = false,
                    errorMessage = "Please fill in all required fields before submitting."
                )
            }
        }
    }
}





