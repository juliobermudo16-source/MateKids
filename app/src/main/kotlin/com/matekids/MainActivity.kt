package com.matekids

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.matekids.domain.model.OperationType
import com.matekids.ui.screen.ChallengeScreen
import com.matekids.ui.screen.CollectionsScreen
import com.matekids.ui.screen.DashboardScreen
import com.matekids.ui.screen.PathScreen
import com.matekids.ui.screen.LessonScreen
import com.matekids.ui.screen.OnboardingScreen
import com.matekids.ui.screen.OperationScreen
import com.matekids.ui.screen.ProblemScreen
import com.matekids.ui.screen.ProfileScreen
import com.matekids.ui.screen.SplashScreen
import com.matekids.ui.screen.StatsScreen
import com.matekids.ui.theme.MateKidsTheme
import com.matekids.ui.viewmodel.CollectionViewModel
import com.matekids.ui.viewmodel.DashboardViewModel
import com.matekids.ui.viewmodel.OperationViewModel
import com.matekids.ui.viewmodel.PathViewModel
import com.matekids.ui.viewmodel.LessonViewModel
import com.matekids.ui.viewmodel.OnboardingViewModel
import com.matekids.ui.viewmodel.StartDestination
import com.matekids.ui.viewmodel.StartupViewModel
import com.matekids.ui.viewmodel.ProblemViewModel
import com.matekids.ui.viewmodel.ProfileViewModel
import com.matekids.ui.viewmodel.StatsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MateKidsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MateKidsApp()
                }
            }
        }
    }
}

@Composable
fun MateKidsApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            val startupViewModel: StartupViewModel = hiltViewModel()
            val destination by startupViewModel.destination.collectAsState()

            SplashScreen(
                onNavigateToDashboard = {
                    // La primera vez se pasa por el alta de perfil; despues, no.
                    val ruta = if (destination == StartDestination.ONBOARDING) {
                        "onboarding"
                    } else {
                        "path"
                    }
                    navController.navigate(ruta) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("onboarding") {
            val viewModel: OnboardingViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            OnboardingScreen(
                uiState = uiState,
                onAliasChange = viewModel::onAliasChange,
                onAvatarSelected = viewModel::onAvatarSelected,
                onContinue = {
                    viewModel.save {
                        navController.navigate("path") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                }
            )
        }

        // El camino de aprendizaje es ahora la pantalla principal.
        composable("path") {
            val viewModel: PathViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            PathScreen(
                uiState = uiState,
                onLessonClick = { lessonId -> navController.navigate("lesson/$lessonId") },
                onProfileClick = { navController.navigate("profile") }
            )
        }

        composable(
            "lesson/{lessonId}",
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId").orEmpty()
            val viewModel: LessonViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            // Genera los ejercicios una sola vez, no en cada recomposicion.
            LaunchedEffect(lessonId) { viewModel.load(lessonId) }

            LessonScreen(
                uiState = uiState,
                onSelectPiece = viewModel::selectPiece,
                onNext = viewModel::next,
                onExit = { navController.popBackStack() }
            )
        }

        composable("dashboard") {
            val viewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            "operations/{type}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val operationType = backStackEntry.arguments?.getString("type")?.let {
                OperationType.valueOf(it)
            } ?: OperationType.SUM
            val viewModel: OperationViewModel = hiltViewModel()
            OperationScreen(
                navController = navController,
                viewModel = viewModel,
                operationType = operationType
            )
        }

        composable(
            "problems/{difficulty}",
            arguments = listOf(
                navArgument("difficulty") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val difficulty = backStackEntry.arguments?.getInt("difficulty") ?: 1
            val viewModel: ProblemViewModel = hiltViewModel()
            ProblemScreen(
                navController = navController,
                viewModel = viewModel,
                difficulty = difficulty
            )
        }

        composable("profile") {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(navController = navController, viewModel = viewModel)
        }

        composable("challenges") {
            ChallengeScreen(navController = navController)
        }

        composable("stats") {
            val viewModel: StatsViewModel = hiltViewModel()
            StatsScreen(navController = navController, viewModel = viewModel)
        }

        composable("collections") {
            val viewModel: CollectionViewModel = hiltViewModel()
            CollectionsScreen(navController = navController, viewModel = viewModel)
        }
    }
}
