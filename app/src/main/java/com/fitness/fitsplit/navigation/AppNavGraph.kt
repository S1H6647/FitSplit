package com.fitness.fitsplit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fitness.fitsplit.ui.screens.auth.ForgotPasswordScreen
import com.fitness.fitsplit.ui.screens.auth.LoginScreen
import com.fitness.fitsplit.ui.screens.auth.SignupScreen
import com.fitness.fitsplit.ui.screens.dashboard.DashboardScreen
import com.fitness.fitsplit.ui.screens.history.HistoryScreen
import com.fitness.fitsplit.ui.screens.profile.ProfileScreen
import com.fitness.fitsplit.ui.screens.split.CreateSplitScreen
import com.fitness.fitsplit.ui.screens.split.NameDaysScreen
import com.fitness.fitsplit.ui.screens.split.SelectDaysScreen
import com.fitness.fitsplit.ui.screens.split.SplitDetailScreen
import com.fitness.fitsplit.ui.screens.split.SplitScreen
import com.fitness.fitsplit.viewModel.SplitViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    topPadding: Dp,
    splitViewModel: SplitViewModel,
    startRoute: String = Routes.LOGIN
) {
    NavHost(
        navController = navController,
        startDestination = startRoute
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
        composable(Routes.DASHBOARD) {
            DashboardScreen(navController, splitViewModel)
        }
        composable(Routes.PROFILE) {
            ProfileScreen(navController)
        }
        composable(Routes.SPLIT) {
            SplitScreen(navController, splitViewModel)
        }
        composable(Routes.HISTORY) {
            HistoryScreen(navController, splitViewModel)
        }
        composable(Routes.CREATE_SPLIT) {
            CreateSplitScreen(navController, splitViewModel)
        }
        composable(Routes.SELECT_DAYS) {
            SelectDaysScreen(navController, splitViewModel)
        }
        composable(Routes.NAME_DAYS) {
            NameDaysScreen(navController, splitViewModel)
        }
        composable(
            route = Routes.SPLIT_DETAIL,
            arguments = listOf(navArgument("splitId") { type = NavType.StringType })
        ) { backStackEntry ->
            val splitId = backStackEntry.arguments?.getString("splitId") ?: ""
            SplitDetailScreen(navController, splitViewModel, splitId)
        }
    }
}