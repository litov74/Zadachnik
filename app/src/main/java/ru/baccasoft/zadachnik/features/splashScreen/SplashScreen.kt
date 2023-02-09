package ru.baccasoft.zadachnik.features.splashScreen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ru.baccasoft.zadachnik.R


@Composable
fun SplashScreen(navController: NavController, isLoggedIn: Boolean) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        val scale = remember {
            Animatable(0f)
        }
        LaunchedEffect(key1 = true) {
            scale.animateTo(
                targetValue = 0.3f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = {
                        OvershootInterpolator(2f).getInterpolation(it)
                    }
                )
            )
            delay(1000L)
            if (isLoggedIn) {
                navController.navigate("main_screen") {
                    popUpTo("splash_screen") {
                        inclusive = true
                    }
                }
            } else {
                navController.navigate("login_screen") {
                    popUpTo("splash_screen") {
                        inclusive = true
                    }
                }
            }

        }
        val image: Painter = painterResource(id = R.drawable.logo2)
        Image(painter = image, contentDescription = "logo", modifier = Modifier.scale(scale.value))
    }

}