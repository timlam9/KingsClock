package com.lamti.kingsclock

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.lamti.kingsclock.ui.screens.ClockPickerScreen
import com.lamti.kingsclock.ui.screens.ClockScreen
import com.lamti.kingsclock.ui.screens.Screen
import com.lamti.kingsclock.ui.theme.KingsClockTheme
import com.lamti.kingsclock.ui.uistate.ChessClock

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        handleCameraCutout()
        setContent {
            KingsClockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val configuration = LocalConfiguration.current
                    val screenHeight = configuration.screenHeightDp.dp

                    var currentScreen: Screen by remember { mutableStateOf(Screen.ClockScreen) }
                    var chessClock: ChessClock by remember { mutableStateOf(ChessClock(0, 30)) }

                    val screenTransition by animateDpAsState(
                        targetValue = if (currentScreen == Screen.PickerScreen) 0.dp else screenHeight,
                        animationSpec = tween(
                            durationMillis = 150,
                            delayMillis = 0,
                            easing = FastOutSlowInEasing
                        ),
                    )

                    when (currentScreen) {
                        Screen.ClockScreen -> ClockScreen(chessClock = chessClock) {
                            currentScreen = Screen.PickerScreen
                        }
                        Screen.PickerScreen -> ClockPickerScreen(
                            modifier = Modifier.offset(y = screenTransition)
                        ) {
                            chessClock = it
                            currentScreen = Screen.ClockScreen
                        }
                    }
                }
            }
        }
    }

    private fun hideSystemUI() {
        //Hides the ugly action bar at the top
        actionBar?.hide()

        //Hide the status bars
        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemBars()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    private fun hideSystemBars() {
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun handleCameraCutout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }
}
