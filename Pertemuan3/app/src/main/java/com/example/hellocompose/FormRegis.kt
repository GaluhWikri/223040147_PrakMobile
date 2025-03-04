package com.example.hellocompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hellocompose.ui.theme.HelloComposeTheme
import com.example.hellocompose.ui.theme.Pink40
import com.example.hellocompose.ui.theme.Purple80

class FormRegis : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormRegister(modifier = Modifier.padding(innerPadding), onNavigateToLogin = { finish() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormRegister(modifier: Modifier = Modifier, onNavigateToLogin: () -> Unit) {
    val username = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val confirmPassword = remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Username", modifier = Modifier.padding(4.dp).fillMaxWidth())
        TextField(
            value = username.value,
            onValueChange = {
                username.value = it
            },
            modifier = Modifier.padding(6.dp).fillMaxWidth()
        )

        Text(text = "Password", modifier = Modifier.padding(4.dp).fillMaxWidth())
        TextField(
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                password.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.padding(6.dp).fillMaxWidth()
        )

        Text(text = "Confirm Password", modifier = Modifier.padding(4.dp).fillMaxWidth())
        TextField(
            value = confirmPassword.value,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                confirmPassword.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.padding(6.dp).fillMaxWidth()
        )

        val registerButtonColors = ButtonDefaults.buttonColors(
            containerColor = Purple80,
            contentColor = Pink40
        )

        Spacer(modifier = Modifier.weight(1f).width(0.dp))

        Row(modifier = Modifier.padding(4.dp).fillMaxWidth()) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    if (password.value.text == confirmPassword.value.text && username.value.text.isNotEmpty() && password.value.text.isNotEmpty()) {
                        Toast.makeText(context, "Registrasi Sukses", Toast.LENGTH_LONG).show()
                        //Di sini Anda bisa menyimpan data ke database atau shared preferences
                        onNavigateToLogin() // Kembali ke layar login setelah registrasi berhasil
                    } else {
                        Toast.makeText(context, "Registrasi Gagal. Pastikan password sama dan semua field terisi.", Toast.LENGTH_LONG).show()
                    }
                },
                colors = registerButtonColors
            ) {
                Text(
                    text = "Register",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}