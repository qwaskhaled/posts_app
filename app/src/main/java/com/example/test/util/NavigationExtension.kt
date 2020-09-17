package com.example.test.util

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigation(
    @IdRes destinationId: Int,
    navDirection: NavDirections) {
    if (currentDestination?.id == destinationId) {
        navigate(navDirection)
    }
}
