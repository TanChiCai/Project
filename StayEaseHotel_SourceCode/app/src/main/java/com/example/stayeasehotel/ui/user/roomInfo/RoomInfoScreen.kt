package com.example.stayeasehotel.ui.user.roomInfo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.stayeasehotel.R
import com.example.stayeasehotel.ui.uiState.HotelRoomUiState
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoomInfoScreen(
    isLoading: Boolean,
    room: HotelRoomUiState,
    checkInMillis: Long?,
    nights: Int?,
    roomCount: Int,
    totalPrice: Double,
    onBookNowClick: (HotelRoomUiState) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern(stringResource(R.string.date_pattern_1))

    val formattedCheckIn = checkInMillis?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            .format(formatter)
    } ?: stringResource(R.string.no_date_selected)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = dimensionResource(R.dimen.dp_32))
                .verticalScroll(rememberScrollState())
        ) {
            // Room Image
            room.image?.let { imageRes ->
                AsyncImage(
                    model = room.image,
                    contentDescription = room.roomType,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.dp_400)), // 180dp
                    contentScale = ContentScale.Crop
                )
            }

            // Room Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.dp_16)) // 16dp
            ) {
                Text(
                    text = room.roomType,
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = dimensionResource(id = R.dimen.sp_20).value.sp, // 20sp
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(R.string.price_per_night, room.pricePerNight),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.dp_4))
                )

                Text(text = stringResource(R.string.capacity_text, room.capacity))
                Text(text = stringResource(R.string.bed_type_text, room.bedType))
                Text(text = stringResource(R.string.size_text, room.squareFoot))
                Text(text = stringResource(R.string.floor_text, room.floor))

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp_8)))

                Text(
                    text = room.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dp_8))
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp_12)))

                Text(
                    text = stringResource(R.string.free_cancellation_info, formattedCheckIn),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF388E3C), // green color to highlight
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp_4)))

                Text(
                    text = stringResource(R.string.requires_confirmation_info),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF388E3C), // green color to emphasize
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp_8)))

                // Amenities
                Text(
                    text = stringResource(R.string.amenities_label),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.dp_4))
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dp_8)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dp_8))
                ) {
                    room.amenities.forEach { amenity ->
                        AssistChip(
                            onClick = {},
                            label = { Text(amenity) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp_4)))

            // Total Payment Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.dp_16)),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp_8)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.dp_16)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dp_8))
                ) {
                    Text(
                        text = stringResource(R.string.total_payment_label),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(
                            R.string.booking_room_price,
                            room.pricePerNight
                        )
                    )

                    Text(
                        text = stringResource(R.string.booking_nights, nights ?: 0)
                    )

                    Text(
                        text = stringResource(R.string.booking_rooms, roomCount)
                    )

                    Divider()

                    Text(
                        text = stringResource(R.string.booking_total, totalPrice),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Button(
                onClick = {
                    onBookNowClick(room)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.dp_12))
                    .height(dimensionResource(R.dimen.dp_40)),
                shape = RoundedCornerShape(dimensionResource(R.dimen.dp_12)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimensionResource(R.dimen.dp_20)),
                        strokeWidth = dimensionResource(R.dimen.dp_1),
                        color = Color.White
                    )
                    Spacer(Modifier.width(dimensionResource(R.dimen.dp_8)))
                    Text(
                        text = "Checking…",
                        fontSize = dimensionResource(R.dimen.sp_20).value.sp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.action_book_now),
                        fontSize = dimensionResource(R.dimen.sp_20).value.sp
                    )
                }
            }
        }
    }

}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RoomInfoScreenPreview() {
    val sampleRoom = HotelRoom(
        roomId = "RM1",
        roomType = "Standard Room",
        pricePerNight = 100.0,
        capacity = 2,
        amenities = listOf("Wi-Fi", "TV", "AC", "Coffee Machine"),
        description = "A comfortable standard room perfect for a short stay.",
        squareFoot = 300,
        floor = "2nd Floor",
        bedType = "Single Bed",
        image = R.drawable.standardroom,
        totalRooms = 10,
        availableRooms = 10
    )

    val sampleCheckInMillis = org.threeten.bp.LocalDate.of(2025, 9, 1)
        .atStartOfDay(org.threeten.bp.ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    RoomInfoScreen(
        room = sampleRoom,
        checkInMillis = sampleCheckInMillis
    )
}*/