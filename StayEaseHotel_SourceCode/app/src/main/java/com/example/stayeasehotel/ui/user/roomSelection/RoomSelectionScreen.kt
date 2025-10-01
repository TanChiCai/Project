package com.example.stayeasehotel

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.stayeasehotel.ui.uiState.HotelRoomUiState
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomSelectionScreen(
    isLoading: Boolean,
    rooms: List<HotelRoomUiState>,
    onBookNowClick: (HotelRoomUiState) -> Unit,
    onRoomDetailsClicked: (HotelRoomUiState) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(rooms) { room ->
            RoomListCard(
                isLoading = isLoading,
                room = room,
                onSelectClick = { onBookNowClick(room) },
                onRoomDetailsClicked = { onRoomDetailsClicked(room) }
            )
        }
    }
}

@Composable
fun RoomListCard(
    isLoading: Boolean,
    room: HotelRoomUiState,
    onSelectClick: () -> Unit,
    onRoomDetailsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        shape = RoundedCornerShape(dimensionResource(R.dimen.dp_12)),
        modifier = Modifier
            .padding(dimensionResource(R.dimen.dp_4))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Room Image
            room.image?.let { imageRes ->
                AsyncImage(
                    model = room.image,
                    contentDescription = stringResource(
                        id = R.string.content_room_image,
                        room.roomType
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.dp_180))
                        .clip(RoundedCornerShape(topStart = dimensionResource(R.dimen.dp_12), topEnd = dimensionResource(R.dimen.dp_12)))
                )
            } ?: Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.dp_180))
                    .background(Color.LightGray)
                    .clip(RoundedCornerShape(topStart = dimensionResource(R.dimen.dp_12), topEnd = dimensionResource(R.dimen.dp_12)))
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.dp_12)))

            // Room Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.dp_12))
            ) {
                Text(
                    text = room.roomType,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = stringResource(R.string.label_room_details),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.dp_4))
                        .clickable{
                            onRoomDetailsClicked()
                        }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            R.string.price_per_night,
                            room.pricePerNight
                        ),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Button(
                        onClick = onSelectClick,
                        shape = RoundedCornerShape(50),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(dimensionResource(R.dimen.dp_16)),
                                strokeWidth = dimensionResource(R.dimen.dp_1),
                                color = Color.White
                            )
                            Spacer(Modifier.width(dimensionResource(R.dimen.dp_8)))
                            Text("Checking…")
                        } else {
                            Text(stringResource(R.string.action_book_now))
                        }
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun RoomSelectionScreenPreview() {
    val sampleRooms = listOf(
        HotelRoomUiState(
            roomId = "1",
            roomType = "Deluxe Room",
            pricePerNight = 250.0,
            capacity = 2,
            amenities = listOf("Wi-Fi", "TV", "Mini Fridge"),
            description = "A cozy deluxe room for 2 guests.",
            squareFoot = 300,
            floor = "2nd Floor",
            bedType = "Queen Bed",
            image = null,
            availableRequests = listOf(
                "Extra pillows",
                "Extra blankets",
                "Extra towels",
                "Extra mattress"
            ),
            totalRooms = 5
        ),
        HotelRoomUiState(
            roomId = "2",
            roomType = "Family Suite",
            pricePerNight = 450.0,
            capacity = 4,
            amenities = listOf("Wi-Fi", "TV", "Kitchenette", "Balcony"),
            description = "Spacious family suite with great view.",
            squareFoot = 500,
            floor = "3rd Floor",
            bedType = "King Bed",
            image = null,
            availableRequests = listOf(
                "Extra pillows",
                "Extra blankets",
                "Extra towels",
                "Extra mattress"
            ),
            totalRooms = 3
        )
    )

    RoomSelectionScreen(
        isLoading = true,
        rooms = sampleRooms,
        onBookNowClick = {},
        onRoomDetailsClicked = {},
        modifier = Modifier.fillMaxSize()
    )
}

