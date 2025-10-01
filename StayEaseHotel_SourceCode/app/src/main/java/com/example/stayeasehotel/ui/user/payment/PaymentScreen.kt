package com.example.stayeasehotel.ui.user.payment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import com.example.stayeasehotel.ui.navigation.PaymentOption
import com.example.stayeasehotel.R
import com.example.stayeasehotel.ui.uiState.BookingUiState
import androidx.compose.ui.text.TextStyle
import com.example.stayeasehotel.ui.viewmodel.BookingViewModel
import com.example.stayeasehotel.ui.navigation.CardFieldError
import com.example.stayeasehotel.ui.ConfirmDialog
import kotlin.text.isDigit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentScreen(
    uiState: BookingUiState,
    viewModel: BookingViewModel,
    onSelectCreditCard: () -> Unit,
    onSelectTouchNGo: () -> Unit,
    onPayNowClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCardSheet by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(dimensionResource(R.dimen.dp_16))
    ) {
        // Header
        Text(
            text = stringResource(R.string.payment_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_8)))

        // Amount
        Text(
            text = stringResource(R.string.total_RM, uiState.totalPrice ?: 0.0),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_32)))

        // Payment Options
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectCreditCard() }
                        .padding(dimensionResource(R.dimen.dp_16)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.paymentOption == PaymentOption.CREDIT_CARD,
                        onClick = { onSelectCreditCard() }
                    )
                    Text(
                        text = stringResource(R.string.credit_debit_card),
                        modifier = Modifier.padding(start = dimensionResource(R.dimen.dp_16))
                    )
                }

                Divider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectTouchNGo() }
                        .padding(dimensionResource(R.dimen.dp_16)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.paymentOption == PaymentOption.CASH,
                        onClick = { onSelectTouchNGo() }
                    )
                    Text(
                        text = stringResource(R.string.cash),
                        modifier = Modifier.padding(start = dimensionResource(R.dimen.dp_16))
                    )
                }
                if (uiState.paymentOption == PaymentOption.CASH) {
                    Text(
                        text = stringResource(R.string.cash_instr),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red,
                        modifier = Modifier.padding(
                            start = dimensionResource(R.dimen.dp_32),
                            bottom = dimensionResource(R.dimen.dp_16)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_32)))

        // Pay Now Button
        Button(
            onClick = {
                if (uiState.paymentOption == PaymentOption.CREDIT_CARD) {
                    showCardSheet = true
                } else {
                    showConfirmDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_40))
        ) {
            Text(text = stringResource(R.string.pay_action))
        }
    }

    if (showCardSheet) {
        CreditDebitBottomSheet(
            uiState = uiState,
            viewModel = viewModel,
            showConfirmDialog = showConfirmDialog,
            onDismiss = { showCardSheet = false },
            onConfirm = {
                showCardSheet = false
                onPayNowClicked()
            },
            onConfirmDialogDismiss = { showConfirmDialog = false },
            onShowConfirmDialog = { showConfirmDialog = true }
        )
    }

    if (showConfirmDialog) {
        ConfirmDialog(
            onConfirm = {
                showCardSheet = false
                viewModel.saveBooking()
                onPayNowClicked()
            },
            onDismiss = { showConfirmDialog = false }
        )
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditDebitBottomSheet(
    uiState: BookingUiState,
    viewModel: BookingViewModel,
    showConfirmDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit, // cardNumber, month, year, cvv
    onConfirmDialogDismiss: () -> Unit,
    onShowConfirmDialog: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)


    val numberTextStyle = TextStyle(
        fontSize = dimensionResource(R.dimen.sp_20).value.sp
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.dp_16)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dp_12))
        ) {
            Text(stringResource(R.string.card_details_instr), style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_8)))

            // Card number
            val numberErrorMsg = cardErrorMessage(uiState.cardNumberError)
            OutlinedTextField(
                value = uiState.cardNumber,
                onValueChange = { input ->
                    val filtered = input.filter { it.isDigit() }.take(16)
                    viewModel.updateCardNumber(filtered)
                },// max 16 digits
                label = { Text(stringResource(R.string.card_number), fontSize = dimensionResource(R.dimen.sp_20).value.sp) },
                textStyle = numberTextStyle,
                isError = numberErrorMsg != null,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                supportingText = {
                    numberErrorMsg?.let { Text(stringResource(it), color = Color.Red, fontSize = dimensionResource(R.dimen.sp_12).value.sp) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Expiration Date (MM/YY)
            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dp_12))) {
                val monthErrorMsg = cardErrorMessage(uiState.expMonthError)
                val yearErrorMsg = cardErrorMessage(uiState.expYearError)
                OutlinedTextField(
                    value = uiState.expMonth,
                    onValueChange = { input ->
                        val filtered = input.filter { it.isDigit() }.take(2)
                        viewModel.updateExpMonth(filtered)
                    }, // max 2 digits
                    label = { Text(stringResource(R.string.card_expiry_month), fontSize = dimensionResource(R.dimen.sp_20).value.sp) },
                    textStyle = numberTextStyle,
                    isError = monthErrorMsg != null,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    supportingText = {
                        monthErrorMsg?.let { Text(stringResource(it), color = Color.Red, fontSize = dimensionResource(R.dimen.sp_12).value.sp) }
                    },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.expYear,
                    onValueChange = { input ->
                        val filtered = input.filter { it.isDigit() }.take(2)
                        viewModel.updateExpYear(filtered)
                    }, // max 2 digits
                    label = { Text(stringResource(R.string.card_expiry_year), fontSize = dimensionResource(R.dimen.sp_20).value.sp) },
                    textStyle = numberTextStyle,
                    isError = yearErrorMsg != null,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    supportingText = {
                        yearErrorMsg?.let { Text(stringResource(it), color = Color.Red, fontSize = dimensionResource(R.dimen.sp_12).value.sp) }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            // CVV
            val cvvErrorMsg = cardErrorMessage(uiState.cvvError)
            OutlinedTextField(
                value = uiState.cvv,
                onValueChange = { input ->
                    val filtered = input.filter { it.isDigit() }.take(3)
                    viewModel.updateCvv(filtered)
                }, // max 4 digits
                label = { Text(stringResource(R.string.card_verification_no), fontSize = dimensionResource(R.dimen.sp_20).value.sp) },
                textStyle = numberTextStyle,
                isError = cvvErrorMsg != null,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation(),
                supportingText = {
                    cvvErrorMsg?.let { Text(stringResource(it), color = Color.Red, fontSize = dimensionResource(R.dimen.sp_12).value.sp) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_16)))

            // Confirm button
            Button(
                onClick = {
                    if (viewModel.canConfirmPayment()) {
                        onShowConfirmDialog()
                    }
                },
                enabled = viewModel.canConfirmPayment(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.confirmation_title))
            }
        }
    }
    if (showConfirmDialog) {
        ConfirmDialog(
            onConfirm = {
                viewModel.saveBooking()
                onConfirm()
            },
            onDismiss = { onConfirmDialogDismiss() }
        )
    }
}



fun cardErrorMessage(error: CardFieldError): Int? {
    return when (error) {
        CardFieldError.EMPTY -> R.string.error_field_required
        CardFieldError.INVALID_LENGTH -> R.string.error_invalid_length
        CardFieldError.INVALID_MONTH -> R.string.error_invalid_month
        CardFieldError.NONE -> null
    }
}