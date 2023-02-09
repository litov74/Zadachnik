package ru.baccasoft.zadachnik.navigation

//import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.baccasoft.zadachnik.features.archiveScreen.ArchiveScreen
import ru.baccasoft.zadachnik.features.loginScreen.LoginScreen
import ru.baccasoft.zadachnik.features.mainScreen.MainScreen
import ru.baccasoft.zadachnik.features.splashScreen.SplashScreen
import timber.log.Timber

//не добавлять аннотацию!!! все равно, что пишет студия, не делай этого, потом не починишь @OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Navigation(
    isLoggedIn: Boolean,
    navController: NavHostController,
    currentDestination: MutableState<String>
) {
    val hintsEnabled = true
    val tag = "NAVIGATION"
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            currentDestination.value = "splash_screen"
            Timber.d(currentDestination.value)
            SplashScreen(navController = navController, isLoggedIn = isLoggedIn)
        }
        composable("main_screen") {
            currentDestination.value = "main_screen"
            Timber.d(currentDestination.value)
            MainScreen(navController = navController, hintsEnabled = hintsEnabled)
        }
        composable("login_screen") {
            currentDestination.value = "login_screen"
            Timber.d(currentDestination.value)
            LoginScreen(navController = navController)
        }
        composable("archive_screen") {
            currentDestination.value = "archive_screen"
            Timber.d(currentDestination.value)
            ArchiveScreen(navController = navController)
        }
    }
}