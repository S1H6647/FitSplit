package com.fitness.fitsplit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fitness.fitsplit.ui.screens.profile.ProfileScreen
import com.fitness.fitsplit.ui.screens.split.SplitScreen
import com.fitness.fitsplit.ui.screens.auth.ForgotPasswordScreen
import com.fitness.fitsplit.ui.screens.auth.LoginScreen
import com.fitness.fitsplit.ui.screens.auth.SignupScreen
import com.fitness.fitsplit.ui.screens.dashboard.DashboardScreen

@Composable
fun AppNavGraph(
    navController : NavHostController,
    topPadding : Dp
){
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(navController)
        }
        composable(Routes.SIGNUP) {
            SignupScreen(navController)
        }
        composable (Routes.DASHBOARD){
            DashboardScreen(navController)
        }
        composable (Routes.PROFILE){
            ProfileScreen(navController)
        }
        composable(Routes.CREATE_SPLIT) {
            SplitScreen(navController)
        }

    }
}