package com.example.stayeasehotel.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.stayeasehotel.ui.viewmodel.UserViewModel


@Composable
fun UserProfilePage(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    // Reload data when this screen is focused
    LaunchedEffect(Unit) {
        userViewModel.loadUserData()
    }

    Scaffold(
        bottomBar = {
            UserBottomAppBar(navController)
        }
    ) { innerPadding ->
        UserProfileContent(innerPadding, navController, userViewModel)
    }
}

@Composable
fun UserProfileContent(
    padding: PaddingValues,
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val userData by userViewModel.userData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        // User Details Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    userData?.let { user ->
                        // User ID (truncated for privacy)
                        ProfileDetailRow("User ID", user.userId)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Name
                        ProfileDetailRow("Name", user.name)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Email
                        ProfileDetailRow("Email", user.email)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Phone Number
                        ProfileDetailRow("Phone", user.phoneNum)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Gender
                        ProfileDetailRow("Gender", user.gender)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Date of Birth
                        ProfileDetailRow("Date of Birth", user.dateOfBirth)
                    } ?: run {
                        // Loading or no data state
                        Text(
                            text = "Loading user information...",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
        // Profile buttons
        UserProfileButtons(navController, userViewModel)
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}