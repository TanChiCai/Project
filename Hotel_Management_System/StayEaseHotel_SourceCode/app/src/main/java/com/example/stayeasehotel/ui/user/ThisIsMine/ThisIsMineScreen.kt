package com.example.stayeasehotel.ui.user.ThisIsMine

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.stayeasehotel.R
import com.example.stayeasehotel.helper.PreviewImageDialog
import com.example.stayeasehotel.shareFilterScreen.FilterDialog
import com.example.stayeasehotel.shareFilterScreen.FilterOptions
import com.example.stayeasehotel.shareFilterScreen.SharedFilterViewModel
import com.example.stayeasehotel.ui.LostItemUi.LostFoundCenterScreen.MyLostClaimItemScreen.MyLostClaimItemScreenBody
import com.example.stayeasehotel.ui.LostItemUi.LostFoundCenterScreen.MyLostClaimItemScreen.MyLostClaimItemScreenViewModel
import com.example.stayeasehotel.ui.ReportLostItemScreen.ImagePickerGrid
import com.example.stayeasehotel.ui.user.ThisIsMine.ThisIsMineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThisIsMineScreen( itemId: String,navController: NavController,viewModel: ThisIsMineViewModel = viewModel()) {

    LaunchedEffect(itemId) {
        viewModel.loadItemById(itemId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },


                title = { Text("This is Mine", style = MaterialTheme.typography.titleMedium) },


            )
        },

        content = { innerPadding ->

            Box(modifier = Modifier.padding(innerPadding)) {
                ThisIsMineScreenBody(viewModel)
            }



        }
    )
}


@Composable
fun ThisIsMineScreenBody(viewModel: ThisIsMineViewModel
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
        //what kind of result want
    ) { uris: List<Uri> ->
        viewModel.setSelectedImages(uris)

    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.addCapturedImage(uiState.cameraImageUri)
            Toast.makeText(context, "Photo saved!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to take photo", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = viewModel.createImageUri(context)
            viewModel.setCameraImageUri(uri)

            takePictureLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }








    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "🔐 Confirm Ownership",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            "Please provide details below to help us verify your claim.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Description Section
        SectionCard(title = "1. Describe the item", showAsterisk = true) {
            ExpandableTextField(
                value = uiState.claimDescription,
                placeholder = "e.g., Red Nike backpack, laptop inside",
                onValueChange = viewModel::setDesription
            )
        }

        // Section 2: Marks
        SectionCard(title = "2. Unique Identifying Marks", showAsterisk = true) {
            ExpandableTextField(
                value = uiState.marks,
                placeholder = "e.g., Scratch near camera, 'Alex' engraved on back",
                onValueChange = viewModel::setMark
            )
        }




        // Section 3: Proof of Ownership
        SectionCard(title = "3. Proof of Ownership(upload if available)", showAsterisk = false) {
            Button(
                onClick = { viewModel.setShowDialog(true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📎 Upload Image / Receipt")
            }


            if (uiState.showDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.setShowDialog(false)
                    },
                    title = { Text("Choose Image Source") },
                    text = { Text("Select an image from gallery or capture using camera") },
                    confirmButton = {
                        TextButton(onClick = {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            viewModel.setShowDialog(false)

                        }) {
                            Text("Camera")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            imagePickerLauncher.launch(arrayOf("image/*"))
                            viewModel.setShowDialog(false)
                        }) {
                            Text("Cancel")
                        }
                        TextButton(onClick = {
                            imagePickerLauncher.launch(arrayOf("image/*"))
                            viewModel.setShowDialog(false)

                        }) {
                            Text("Gallery")
                        }
                    }
                )
            }


            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.selectedImages.forEachIndexed { index, uri ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { viewModel.setPreviewIndexChange(index) },
                            shape = MaterialTheme.shapes.medium,
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = "Selected image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        IconButton(
                            onClick = { viewModel.removeSelectedImage(index) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = MaterialTheme.shapes.small
                                )
                                .size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove image",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            // Full-screen preview dialog
            if (uiState.previewIndex in uiState.selectedImages.indices) {
                Dialog(onDismissRequest = { viewModel.setPreviewIndexChange(-1) }) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                            .clickable { viewModel.setPreviewIndexChange(-1) },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(uiState.selectedImages[uiState.previewIndex]),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.8f)
                        )
                    }
                }
            }


        }

        // Additional Notes Section
        SectionCard(title = "4. Additional Notes (Optional)", showAsterisk = false) {
            ExpandableTextField(
                value = uiState.notes,
                placeholder = "Any other helpful info...",
                onValueChange = viewModel::setNote
            )
        }


        Button(
            onClick = {
                viewModel.submitClaim()
             },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Submit Claim",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (uiState.submissionSuccess) {
            AlertDialog(
                onDismissRequest = { viewModel.clearMessages() },
                confirmButton = {
                    Button(onClick = { viewModel.clearMessages() }) {
                        Text("OK")
                    }
                },
                title = { Text("Success") },
                text = {Text(
                    "Submission completed successfully.\n\n" +
                            "Claim ID: ${uiState.claimId}\n\n" +
                            "Please keep this ID for your records. You may need it to check your claim status."
                ) }


            )
        }


        if (uiState.errorMessage != null) {
            AlertDialog(
                onDismissRequest = { viewModel.clearMessages() },
                confirmButton = {
                    Button(onClick = { viewModel.clearMessages() }) {
                        Text("OK")
                    }
                },
                title = { Text("Error") },
                text = { Text(uiState.errorMessage ?: "Unknown error") }
            )
        }

        if (uiState.isSubmitting) {
            Dialog(onDismissRequest = { /* block back press too */ }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }

}

@Composable
fun SectionCard(title: String,showAsterisk: Boolean = false,  content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row {

                Text(title+":", style = MaterialTheme.typography.titleMedium)
                if (showAsterisk) {
                    Text(
                        text = " *",
                        color = Color.Red,
                        fontSize = 16.sp
                    )



                }
            }
            content()
        }
    }
}

@Composable
fun ExpandableTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    minLines: Int = 3,
    maxLines: Int = 7
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        minLines = minLines,
        maxLines = maxLines,
        singleLine = false
    )
}

