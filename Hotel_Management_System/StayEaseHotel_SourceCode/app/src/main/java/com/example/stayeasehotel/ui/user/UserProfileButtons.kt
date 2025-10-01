package com.example.stayeasehotel.ui.user


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.stayeasehotel.R

import com.example.stayeasehotel.data.UserProfileBs
import com.example.stayeasehotel.model.UserProfileB
import com.example.stayeasehotel.ui.viewmodel.UserViewModel

@Composable
fun UserProfileButtons(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val userProfileButtons = UserProfileBs.UserProfileBarList

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        userProfileButtons.forEach { profileButton ->
            UserProfileButton(
                userProfileB = profileButton,
                onClick = {
                    when (profileButton.buttonUserBName) {
                        R.string.userP1 -> navController.navigate("user_about_us")
                        R.string.userP2 -> navController.navigate("user_logout")
                        R.string.userP3 -> navController.navigate("user_delete_account")
                    }
                }
            )
        }
    }
}

@Composable
fun UserProfileButton(
    userProfileB: UserProfileB,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = userProfileB.buttonUserBImage),
                contentDescription = stringResource(id = userProfileB.buttonUserBName),
                tint = Color.Unspecified,
                modifier = Modifier.size(50.dp)
            )

            Text(
                text = stringResource(id = userProfileB.buttonUserBName),
                fontSize = 30.sp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}