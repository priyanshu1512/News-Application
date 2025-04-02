package eu.tutorials.mynewsapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eu.tutorials.myrecipeapp.Article
import eu.tutorials.myrecipeapp.ui.screens.SettingsScreen

@Composable
fun NewsApp(navController: NavHostController) {
    val newsViewModel: MainViewModel = viewModel()
    val viewstate by newsViewModel.newsState
    val selectedTab by newsViewModel.selectedTab
    val selectedCategory by newsViewModel.selectedCategory

    NavHost(navController = navController, startDestination = Screen.NewsScreen.route) {
        composable(route = Screen.NewsScreen.route) {
            NewsScreen(
                viewstate = viewstate,
                selectedTab = selectedTab,
                onTabSelected = { newsViewModel.setSelectedTab(it) },
                selectedCategory = selectedCategory,
                onCategorySelected = { newsViewModel.fetchNewsByCategory(it) },
                navigateToDetail = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("article", it)
                    navController.navigate(Screen.DetailScreen.route)
                },
                navController = navController  // Add this parameter
            )
        }
        composable(route = Screen.DetailScreen.route) {
            val article = navController.previousBackStackEntry?.savedStateHandle?.
            get<Article>("article")
            if (article != null) {
                NewsDetailScreen(article = article)
            }
        }
        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen()
        }
    }
}