package com.fitness.fitsplit.ui

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fitness.fitsplit.navigation.AppNavGraph
import com.fitness.fitsplit.navigation.Routes
import com.fitness.fitsplit.navigation.bottomNavItems
import com.fitness.fitsplit.navigation.screensWithBottomBar

@Composable
fun MainApp(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val selectedGraph = when {
        currentRoute?.startsWith(Routes.DASHBOARD_GRAPH) == true -> Routes.DASHBOARD_GRAPH
        currentRoute?.startsWith(Routes.SPLIT_GRAPH) == true -> Routes.SPLIT_GRAPH
        currentRoute?.startsWith(Routes.PROFILE_GRAPH) == true -> Routes.PROFILE_GRAPH
        else -> Routes.DASHBOARD_GRAPH
    }

    val showBottomBar = currentRoute in screensWithBottomBar


    Scaffold(
        bottomBar = {
            if (showBottomBar){
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    Log.d("Check", "$currentRoute")

                    bottomNavItems.forEach { item ->
                        Log.d("Check", item.graph)
                        NavigationBarItem(
                            selected = currentRoute == item.graph,
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
        AppNavGraph(navController, topPadding = innerPadding.calculateTopPadding())
    }
}