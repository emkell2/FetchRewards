package com.fetchrewards.samplelist.sortedlist.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fetchrewards.samplelist.sortedlist.constants.RouteConstants.BUTTON_SCREEN_ROUTE
import com.fetchrewards.samplelist.sortedlist.constants.RouteConstants.SORTED_LIST_SCREEN_ROUTE
import com.fetchrewards.samplelist.sortedlist.ui.screens.ButtonScreen
import com.fetchrewards.samplelist.sortedlist.ui.screens.SortedListScreen
import com.fetchrewards.samplelist.sortedlist.ui.theme.SampleListTheme

@Composable
fun SampleListNavGraph() {
    val navController = rememberNavController()

    SampleListTheme {
        NavHost(
            navController = navController,
            startDestination = BUTTON_SCREEN_ROUTE
        ) {
            composable(
                route = BUTTON_SCREEN_ROUTE
            ) {
                ButtonScreen(
                    onBtnClick = {
                        navController.navigate(SORTED_LIST_SCREEN_ROUTE)
                    }
                )
            }

            composable(
                route = SORTED_LIST_SCREEN_ROUTE
            ) {
                SortedListScreen(
                    onNavigateInitialScreen = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}