package com.example.stayeasehotel.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun UserHomePage(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            UserBottomAppBar(navController) // Your bottom navigation bar
        }
    ) { innerPadding ->
        UserHomeContent(innerPadding)
    }
}

@Composable
fun UserHomeContent(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        // Your home page content here
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(300.dp)
                .background(Color.Yellow),
            contentAlignment = Alignment.Center
        ){
            Text(
                "Welcome to Stay Ease Hotel",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                textAlign = TextAlign.Center,
                lineHeight = 70.sp,
                modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(10.dp))

    }
}