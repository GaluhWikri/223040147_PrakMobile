package com.example.hellocompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hellocompose.ui.theme.HelloComposeTheme

class RegistrationForm : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegistrationForm(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RegistrationForm(modifier: Modifier = Modifier) {
    // States untuk menyimpan nilai input
    val namaState = remember { mutableStateOf(TextFieldValue("")) }
    val usernameState = remember { mutableStateOf(TextFieldValue("")) }
    val teleponState = remember { mutableStateOf(TextFieldValue("")) }
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val alamatState = remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current // Untuk menampilkan Toast

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Agar elemen berada di tengah
    ) {
        Text(
            text = "Form Registrasi",
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Input Nama
        OutlinedTextField(
            value = namaState.value,
            onValueChange = { namaState.value = it },
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input Username
        OutlinedTextField(
            value = usernameState.value,
            onValueChange = { usernameState.value = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input Nomor Telepon
        OutlinedTextField(
            value = teleponState.value,
            onValueChange = { teleponState.value = it },
            label = { Text("Nomor Telepon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input Email
        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input Alamat
        OutlinedTextField(
            value = alamatState.value,
            onValueChange = { alamatState.value = it },
            label = { Text("Alamat") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol-Tombol
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround // Mengatur jarak antar tombol
        ) {
            // Tombol Simpan
            Button(
                onClick = {
                    if (namaState.value.text.isNotEmpty() &&
                        usernameState.value.text.isNotEmpty() &&
                        teleponState.value.text.isNotEmpty() &&
                        emailState.value.text.isNotEmpty() &&
                        alamatState.value.text.isNotEmpty()
                    ) {
                        Toast.makeText(context, "Halo, ${namaState.value.text}!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Semua inputan harus diisi!", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Simpan")
            }

            // Tombol Reset
            Button(
                onClick = {
                    namaState.value = TextFieldValue("")
                    usernameState.value = TextFieldValue("")
                    teleponState.value = TextFieldValue("")
                    emailState.value = TextFieldValue("")
                    alamatState.value = TextFieldValue("")
                }
            ) {
                Text("Reset")
            }
        }
    }
}