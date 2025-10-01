package com.example.stayeasehotel.ui.user

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.stayeasehotel.ui.navigation.BookingNavigation
import com.example.stayeasehotel.ui.viewmodel.BookingViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserRoomManagement(
    navController: NavHostController,
    bookingViewModel: BookingViewModel
) {
    val innerNavController = rememberNavController()
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Only show bottom bar on the DateSelection screen
    val bottomBarRoutes = listOf(
        BookingNavigation.DateSelection.name
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                UserBottomAppBar(navController) // main bottom bar
            }
        }
    ) { innerPadding ->
        BookingNavigation(
            bookingViewModel = bookingViewModel,
            navController = innerNavController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

