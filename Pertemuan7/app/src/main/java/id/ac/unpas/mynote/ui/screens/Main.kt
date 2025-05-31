package id.ac.unpas.mynote.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    DestinationsNavHost(
        navController = navController,
        navGraph = NavGraphs.root,
        modifier = modifier
    )
}
