package com.example.stayeasehotel.ui


import com.example.stayeasehotel.model.StaffEntity
import com.example.stayeasehotel.model.UserEntity


object UserSession {
    var currentUser: UserEntity? = null
}

object StaffSession {
    var currentStaff: StaffEntity? = null
}
