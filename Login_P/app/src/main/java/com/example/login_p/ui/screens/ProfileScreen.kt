package com.example.login_p.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(navController: NavController, userEmail: String) {
    var userName by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Firestore'dan kullanıcı adı çek
    LaunchedEffect(userEmail) {
        if (userEmail.isNotEmpty()) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener { documents ->
                    val name = documents.firstOrNull()?.getString("name")
                    userName = name
                    isLoading = false
                }
                .addOnFailureListener {
                    userName = null
                    isLoading = false
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            Text("Yükleniyor...")
        } else {
            Text("Kullanıcı Adı: ${userName ?: "Bilinmiyor"}")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                navController.navigate("login") {
                    popUpTo("profile/{email}") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Çıkış Yap")
        }
    }
} 