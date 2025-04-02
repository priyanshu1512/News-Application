package eu.tutorials.mynewsapp

sealed class Screen(val route: String) {
    object NewsScreen: Screen("newsscreen")
    object DetailScreen: Screen("detailscreen")
    object SettingsScreen: Screen("settingsscreen")

}