package com.example.stayeasehotel.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.stayeasehotel.R
import com.example.stayeasehotel.ui.viewmodel.UserSigninViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSignin(navController: NavHostController) {
    val viewModel: UserSigninViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Sign Up") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(780.dp)
                    .background(Color.Blue)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "User Sign Up",
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Box(
                        modifier = Modifier.width(350.dp)
                            .height(660.dp)
                            .background(Color.White)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            // Name Field
                            OutlinedTextField(
                                value = uiState.name,
                                onValueChange = { viewModel.updateField("name", it) },
                                label = { Text("Full Name") },
                                modifier = Modifier.width(260.dp)
                            )

                            // Email Field
                            OutlinedTextField(
                                value = uiState.email,
                                onValueChange = { viewModel.updateField("email", it) },
                                label = { Text("Email") },
                                placeholder = { Text("example@gmail.com") },
                                modifier = Modifier.width(260.dp),
                                isError = uiState.email.isNotBlank() && !viewModel.isValidEmail(uiState.email),
                                supportingText = {
                                    if (uiState.email.isNotBlank() && !viewModel.isValidEmail(uiState.email)) {
                                        Text("Must be example@gmail.com")
                                    }
                                }
                            )

                            // Phone Number Fields
                            Row(
                                Modifier.width(260.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Area Code
                                OutlinedTextField(
                                    value = uiState.areaCode,
                                    onValueChange = { viewModel.updateField("areaCode", it.filter { it.isDigit() }.take(3)) },
                                    label = { Text("Code") },
                                    placeholder = { Text("012") },
                                    modifier = Modifier.weight(1f),
                                    isError = uiState.areaCode.isNotBlank() && uiState.areaCode.length != 3
                                )

                                // Separator
                                Text("-", modifier = Modifier.padding(top = 16.dp))

                                // Main Number
                                OutlinedTextField(
                                    value = uiState.phoneNumber,
                                    onValueChange = { viewModel.updateField("phoneNumber", it.filter { it.isDigit() }.take(8)) },
                                    label = { Text("Number") },
                                    placeholder = { Text("3456789") },
                                    modifier = Modifier.weight(2f),
                                    isError = uiState.phoneNumber.isNotBlank() && uiState.phoneNumber.length < 7
                                )
                            }

                            var isGenderExpanded by remember { mutableStateOf(false) }
                            val genders = listOf("Male", "Female")

                            // Gender Dropdown
                            ExposedDropdownMenuBox(
                                expanded = isGenderExpanded,
                                onExpandedChange = { isGenderExpanded = it },
                                modifier = Modifier.width(260.dp)
                            ) {
                                OutlinedTextField(
                                    value = uiState.gender,
                                    onValueChange = {},
                                    label = { Text("Gender") },
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = isGenderExpanded
                                        )
                                    },
                                    modifier = Modifier.menuAnchor().width(260.dp),
                                    isError = uiState.gender.isBlank()
                                )

                                ExposedDropdownMenu(
                                    expanded = isGenderExpanded,
                                    onDismissRequest = { isGenderExpanded = false }
                                ) {
                                    genders.forEach { selectionOption ->
                                        DropdownMenuItem(
                                            text = { Text(selectionOption) },
                                            onClick = {
                                                viewModel.updateField("gender", selectionOption)
                                                isGenderExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Date of Birth Fields
                            Row(
                                Modifier.width(260.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Day
                                OutlinedTextField(
                                    value = uiState.day,
                                    onValueChange = { viewModel.updateField("day", it.filter { it.isDigit() }.take(2)) },
                                    label = { Text("DD") },
                                    placeholder = { Text("DD") },
                                    modifier = Modifier.weight(1f),
                                    isError = uiState.day.isNotBlank() && (uiState.day.length != 2 || uiState.day.toIntOrNull() !in 1..31)
                                )

                                // Month
                                OutlinedTextField(
                                    value = uiState.month,
                                    onValueChange = { viewModel.updateField("month", it.filter { it.isDigit() }.take(2)) },
                                    label = { Text("MM") },
                                    placeholder = { Text("MM") },
                                    modifier = Modifier.weight(1f),
                                    isError = uiState.month.isNotBlank() && (uiState.month.length != 2 || uiState.month.toIntOrNull() !in 1..12)
                                )

                                OutlinedTextField(
                                    value = uiState.year,
                                    onValueChange = { viewModel.updateField("year", it.filter { it.isDigit() }.take(4)) },
                                    label = { Text("YYYY") },
                                    placeholder = { Text("YYYY") },
                                    modifier = Modifier.weight(1.5f),
                                    isError = uiState.year.isNotBlank() && (uiState.year.length != 4 || uiState.year.toIntOrNull() !in 1900..2100)
                                )
                            }

                            OutlinedTextField(
                                value = uiState.password,
                                onValueChange = { viewModel.updateField("password", it) },
                                label = { Text("Password") },
                                modifier = Modifier.width(260.dp),
                                isError = uiState.password.isNotBlank() && uiState.password.length < 6,
                                supportingText = {
                                    if (uiState.password.isNotBlank() && uiState.password.length < 6) {
                                        Text("Min 6 characters")
                                    }
                                }
                            )

                            OutlinedTextField(
                                value = uiState.confirmPassword,
                                onValueChange = { viewModel.updateField("confirmPassword", it) },
                                label = { Text("Confirm Password") },
                                modifier = Modifier.width(260.dp),
                                isError = uiState.confirmPassword.isNotBlank() && uiState.password != uiState.confirmPassword,
                                supportingText = {
                                    if (uiState.confirmPassword.isNotBlank() && uiState.password != uiState.confirmPassword) {
                                        Text("Passwords don't match")
                                    }
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .width(260.dp)
                                    .height(60.dp)
                            ) {
                                uiState.errorMessage?.let {
                                    Text(
                                        text = it,
                                        color = Color.Red,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.Center),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        softWrap = true
                                    )
                                }
                            }



                            if (uiState.isLoading) {
                                CircularProgressIndicator()
                            } else {
                                Button(
                                    onClick = {
                                        viewModel.signUp(
                                            onSuccess = {
                                                navController.navigate("user_success") {
                                                    popUpTo(0) }
                                            },
                                            onError = { /* Error handled in state */ }
                                        )
                                    },
                                    modifier = Modifier
                                ) {
                                    Text(
                                        "Sign Up",
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}