package com.fitness.fitsplit.navigation

import com.fitness.fitsplit.R

object Routes {
    // Bottom bar tabs
    const val DASHBOARD_GRAPH = "dashboard"
    const val SPLIT_GRAPH = "split"
    const val PROFILE_GRAPH = "profile"

    const val SPLIT = "split"
    const val PROFILE = "profile"

    // Auth screens
    const val LOGIN = "login"
    const val FORGOT_PASSWORD = "forgot_password"
    const val SIGNUP = "signup"
    
    // Main screens
    const val DASHBOARD = "dashboard"
    const val CREATE_SPLIT = "split"
    const val SELECT_DAYS = "select_days"
    const val DAY_DETAIL = "day_detail"
    const val ADD_EXERCISE = "add_exercise"
}

data class BottomNavItem (
    val graph : String,
    val label : String,
    val icon : Int,
)

val bottomNavItems = listOf(
    BottomNavItem(Routes.DASHBOARD_GRAPH, "Home", R.drawable.home),
    BottomNavItem(Routes.SPLIT_GRAPH, "Split", R.drawable.list),
    BottomNavItem(Routes.PROFILE_GRAPH, "Profile", R.drawable.user)
)

val screensWithBottomBar = listOf(
    Routes.DASHBOARD,
    Routes.SPLIT,
    Routes.PROFILE
)