package com.example.stayeasehotel.ui.user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.stayeasehotel.ui.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserLogOut(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    // Handle system back button
    BackHandler {
        FirebaseAuth.getInstance().signOut()
        userViewModel.triggerStateChoiceNavigation()
    }

    Scaffold() { innerPadding ->
        UserLogOutContent(innerPadding, userViewModel)
    }
}

@Composable
fun UserLogOutContent(
    padding: PaddingValues,
    userViewModel: UserViewModel
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
            text = "Log Out Successful!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                userViewModel.clearUserData()
                userViewModel.triggerStateChoiceNavigation()
            }
        ) {
            Text("Ok")
        }
    }
}