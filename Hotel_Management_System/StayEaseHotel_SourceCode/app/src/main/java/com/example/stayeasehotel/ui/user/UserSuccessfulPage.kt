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

@Composable
fun UserSuccessfulPage(navController: NavHostController) {

    BackHandler(enabled = true) {
        navController.navigate("state_choice") {
            popUpTo(0) { inclusive = true }
        }
    }

    Scaffold { innerPadding ->
        UserSuccessful(innerPadding = innerPadding, navController)
    }
}

@Composable
fun UserSuccessful(
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Log Out Successful!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Button(
            onClick = {
                navController.navigate("state_choice") {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        ) {
            Text("Ok")
        }
    }
}