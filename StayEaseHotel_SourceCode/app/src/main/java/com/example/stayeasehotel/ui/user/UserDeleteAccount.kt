package com.example.stayeasehotel.ui.user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.stayeasehotel.ui.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDeleteAccount(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val isLoading = remember { mutableStateOf(false) }

    // Handle system back button
    BackHandler {
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Delete Account") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            UserBottomAppBar(navController)
        }
    ) { innerPadding ->
        UserDeleteAccountContent(innerPadding, userViewModel, isLoading)
    }
}

@Composable
fun UserDeleteAccountContent(
    padding: PaddingValues,
    userViewModel: UserViewModel,
    isLoading: MutableState<Boolean>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
    ) {
        Text(
            text = "Delete Account",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Warning: This action cannot be undone. All your data will be permanently deleted.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (isLoading.value) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    isLoading.value = true
                    deleteUserAccount(userViewModel, isLoading)
                }
            ) {
                Text("Delete My Account")
            }
        }
    }
}

private fun deleteUserAccount(
    userViewModel: UserViewModel,
    isLoading: MutableState<Boolean>
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    if (currentUser != null) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. Delete user data from Firestore
                db.collection("Users").document(currentUser.uid).delete().await()

                // 2. Delete authentication account
                currentUser.delete().await()

                // 3. Trigger navigation to state_choice
                CoroutineScope(Dispatchers.Main).launch {
                    userViewModel.triggerStateChoiceNavigation()
                }
            } catch (e: Exception) {
                isLoading.value = false
            }
        }
    } else {
        isLoading.value = false
        userViewModel.triggerStateChoiceNavigation()
    }
}