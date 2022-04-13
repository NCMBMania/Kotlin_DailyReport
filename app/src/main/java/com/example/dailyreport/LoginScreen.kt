package com.example.dailyreport

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nifcloud.mbaas.core.NCMBException
import com.nifcloud.mbaas.core.NCMBUser

@Composable
fun LoginScreen(navController: NavController, callback: () -> Unit) {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var progress by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val login = {
        try {
            progress = true
            val user = NCMBUser()
            user.userName = userName
            user.password = password
            user.login()
            callback()
        } catch(e: NCMBException){
            showDialog = true
        }
        progress = false
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            buttons = {
                Button(onClick = {
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            title = {Text("認証エラー")},
            text = {Text("認証できませんでした（${userName}）")}
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            modifier = Modifier.padding(20.dp),
            label = { Text("ユーザ名") },
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.padding(20.dp),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("パスワード") },
        )
        Button(onClick = login,
            enabled = !progress,
            modifier = Modifier.padding(20.dp)
        ) {
            if (progress) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("ログイン")
            }
        }
    }
}