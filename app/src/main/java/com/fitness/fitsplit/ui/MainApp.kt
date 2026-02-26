package com.fitness.fitsplit.ui

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fitness.fitsplit.navigation.AppNavGraph
import com.fitness.fitsplit.navigation.Routes
import com.fitness.fitsplit.navigation.bottomNavItems
import com.fitness.fitsplit.navigation.screensWithBottomBar
import com.fitness.fitsplit.repository.split.SplitRepoImpl
import com.fitness.fitsplit.viewModel.SplitViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Check if user is already logged in
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startRoute = if (currentUser != null) Routes.DASHBOARD else Routes.LOGIN

    // Shared SplitViewModel lifted here so bottom bar can observe hasSplits
    val splitViewModel = remember { SplitViewModel(repo = SplitRepoImpl()) }
    val hasSplits by splitViewModel.hasSplits
    val splitsChecked by splitViewModel.splitsChecked

    // Show bottom bar only when user has splits AND is on a main screen
    val showBottomBar = splitsChecked && hasSplits && currentRoute in screensWithBottomBar

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val entry by navController.currentBackStackEntryAsState()
                    val route = entry?.destination?.route

                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = route == item.graph,
                            onClick = {
                                navController.navigate(item.graph) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(painterResource(item.icon), contentDescription = item.label)
                            },
                            label = {
                                Text(item.label)
                            },
                            alwaysShowLabel = true
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            topPadding = innerPadding.calculateTopPadding(),
            splitViewModel = splitViewModel,
            startRoute = startRoute
        )
    }
}