package com.example.stayeasehotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.stayeasehotel.ui.uiState.HotelRoomUiState
import com.example.stayeasehotel.data.repository.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RoomViewModel(
    private val roomRepository: RoomRepository
): ViewModel() {
    private val _rooms = MutableStateFlow<List<HotelRoomUiState>>(emptyList())
    val rooms: StateFlow<List<HotelRoomUiState>> = _rooms.asStateFlow()


    init {
        fetchRooms()
    }

    fun fetchRooms() {
        roomRepository.fetchRooms { roomList ->
            _rooms.value = roomList
        }
    }
}