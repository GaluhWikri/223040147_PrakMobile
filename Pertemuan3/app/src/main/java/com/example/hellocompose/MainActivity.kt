package com.example.hellocompose

import android.content.Intent
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormLogin(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FormLogin(modifier: Modifier = Modifier) {
    val username = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
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

        val loginButtonColors = ButtonDefaults.buttonColors(
            containerColor = Purple80,
            contentColor = Pink40
        )

        val resetButtonColors = ButtonDefaults.buttonColors(
            containerColor = Pink40,
            contentColor = Purple80
        )

        Spacer(modifier = Modifier.weight(1f).width(0.dp))

        Row(modifier = Modifier.padding(4.dp).fillMaxWidth()) {
            // Tombol Login
            Button(
                modifier = Modifier.weight(5f),
                onClick = {
                    if (username.value.text == "admin" && password.value.text == "admin") {
                        Toast.makeText(context, "Login Sukses", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Login Gagal", Toast.LENGTH_LONG).show()
                    }
                },
                colors = loginButtonColors
            ) {
                Text(
                    text = "Login",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Tombol Reset
            Button(
                modifier = Modifier.weight(5f),
                onClick = {
                    // Reset Username dan Password
                    username.value = TextFieldValue("")
                    password.value = TextFieldValue("")
                    Toast.makeText(context, "Form direset", Toast.LENGTH_SHORT).show()
                },
                colors = resetButtonColors
            ) {
                Text(
                    text = "Reset",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        // Tombol Register
        val context = LocalContext.current
        Button(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            onClick = {
                val intent = Intent(context, FormRegis::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White)
        ) {
            Text(text = "Register", style = TextStyle(color = Color.White, fontSize = 18.sp))
        }
    }
}