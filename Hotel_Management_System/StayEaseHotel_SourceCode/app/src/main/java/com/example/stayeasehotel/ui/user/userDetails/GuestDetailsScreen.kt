package com.example.stayeasehotel.ui.user.userDetails

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.stayeasehotel.R
import com.example.stayeasehotel.ui.uiState.BookingUiState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GuestDetailsScreen(
    uiState: BookingUiState,
    onToggleRequest: (String) -> Unit,
    onNextClicked: () -> Unit,
    modifier: Modifier
) {
    val formatter = DateTimeFormatter.ofPattern(stringResource(R.string.date_pattern_2))
    val checkInText = uiState.checkInDate?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(formatter)
    } ?: stringResource(R.string.no_date_selected)

    val checkOutText = uiState.checkOutDate?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(formatter)
    } ?: stringResource(R.string.no_date_selected)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.dp_16),
                    end = dimensionResource(R.dimen.dp_16),
                    bottom = dimensionResource(R.dimen.dp_40)
                )
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.username_label),
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.dp_16), top = dimensionResource(R.dimen.dp_40))
                    .align(alignment = Alignment.Start)
            )

            Text(
                text = uiState.userName,
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.dp_32))
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = stringResource(R.string.email_label),
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.dp_16))
                    .align(alignment = Alignment.Start)
            )

            Text(
                text = uiState.userEmail ?: "",
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.dp_32))
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge
            )

            // Phone Number Section
            Text(
                text = stringResource(R.string.phone_label),
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.dp_16))
                    .align(alignment = Alignment.Start)
            )

            Text(
                text = uiState.userPhone,
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.dp_32))
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge
            )

            uiState.selectedRoom?.let { room ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(R.dimen.dp_32)),
                    elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.dp_4)),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.dp_4))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.dp_16)),
                        verticalAlignment = Alignment.Top
                    ) {
                        room.image?.let { imageRes ->
                            AsyncImage(
                                model = room.image,
                                contentDescription = room.roomType,
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.dp_120))
                                    .clip(RoundedCornerShape(dimensionResource(R.dimen.dp_4))),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(
                            modifier = Modifier.padding(start = dimensionResource(R.dimen.dp_16))
                        ) {
                            Text(
                                text = room.roomType,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_8)))

                            Text(
                                text = stringResource(R.string.check_in_label, checkInText)
                            )
                            Text(
                                text = stringResource(R.string.check_out_label, checkOutText)
                            )
                            Text(
                                text = stringResource(R.string.booking_nights, uiState.nights ?: 0)
                            )
                            Text(
                                text = stringResource(R.string.booking_rooms, uiState.roomCount)
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_8)))

                            Text(
                                text = stringResource(R.string.total_price_label, uiState.totalPrice ?: 0.0),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_16)))

                // Special Requests Section
                Text(
                    text = stringResource(R.string.special_requests_label),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start)
                )

                uiState.selectedRoom.availableRequests.forEach { request ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimensionResource(R.dimen.dp_4))
                    ) {
                        Checkbox(
                            checked = uiState.selectedRequests.contains(request),
                            onCheckedChange = { onToggleRequest(request) }
                        )
                        Text(
                            text = request,
                            modifier = Modifier.clickable{ onToggleRequest(request) }
                        )
                    }
                }

                // Final Step Button
                Button(
                    onClick = { onNextClicked() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.dp_12))
                        .height(dimensionResource(R.dimen.dp_40)),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.dp_12))
                ) {
                    Text(
                        text = stringResource(R.string.action_final_step),
                        fontSize = dimensionResource(R.dimen.sp_20).value.sp
                    )
                }
            }
        }
    }

}

/*@Preview(showBackground = true)
@Composable
fun GuestDetailsPreview() {
    GuestDetailsScreen(modifier = Modifier)
}*/