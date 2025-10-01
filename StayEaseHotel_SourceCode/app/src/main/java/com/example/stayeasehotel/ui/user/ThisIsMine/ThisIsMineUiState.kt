package com.example.stayeasehotel.ui.user.ThisIsMine

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.stayeasehotel.data.LostItemData.LostItemEntity


data class ThisIsMineUiState(
    val loadedItem: LostItemEntity? = null,
    val claimId:String="",
    val claimDescription :String="",
    val marks :String="",
    val notes :String="",
    val claimTime: Long = System.currentTimeMillis(),
    val cameraImageUri: Uri? = null,
    val selectedImages: List<Uri> = emptyList(),
    val previewIndex: Int = -1,
    val showSuccessDialog : Boolean= false,
    val isSubmitting: Boolean = false,
    val submissionSuccess: Boolean = false,
    val errorMessage: String? = null,
    val showDialog: Boolean =false,
    val itemStatus: String? = ""

)
