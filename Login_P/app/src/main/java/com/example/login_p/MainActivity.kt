package com.example.login_p

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.login_p.ui.theme.Login_PTheme
import com.example.login_p.ui.screens.LoginScreen
import com.example.login_p.ui.screens.HomeScreen
import com.example.login_p.ui.screens.ProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Login_PTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var userEmail by remember { mutableStateOf("") }

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        bottomBar = {
                            if (currentRoute == "home" || (currentRoute?.startsWith("profile") == true)) {
                                NavigationBar {
                                    NavigationBarItem(
                                        selected = currentRoute == "home",
                                        onClick = {
                                            if (currentRoute != "home") {
                                                navController.navigate("home")
                                            }
                                        },
                                        icon = { Icon(Icons.Filled.Home, contentDescription = "Anasayfa") },
                                        label = { Text("Anasayfa") }
                                    )
                                    NavigationBarItem(
                                        selected = currentRoute?.startsWith("profile") == true,
                                        onClick = {
                                            if (currentRoute?.startsWith("profile") != true) {
                                                navController.navigate("profile/${userEmail}")
                                            }
                                        },
                                        icon = { Icon(Icons.Filled.Person, contentDescription = "Kullanıcı") },
                                        label = { Text("Kullanıcı") }
                                    )
                                }
                            }
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = "login",
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable("login") {
                                LoginScreen(
                                    navController = navController,
                                    onLoginSuccess = { email ->
                                        userEmail = email
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("home") {
                                HomeScreen(navController = navController)
                            }
                            composable(
                                "profile/{email}",
                                arguments = listOf(navArgument("email") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val email = backStackEntry.arguments?.getString("email") ?: ""
                                ProfileScreen(navController = navController, userEmail = email)
                            }
                        }
                    }
                }
            }
        }
    }
}