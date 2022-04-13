package com.example.dailyreport

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nifcloud.mbaas.core.*

@Composable
fun FormScreen(navController: NavController, obj: NCMBObject) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    if (obj.getObjectId() != null) {
        title = obj.getString("title")!!
        body = obj.getString("body")!!
    }
    var progress by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
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
            title = {Text("日報保存")},
            text = {Text(message)}
        )
    }

    val save = {
        progress = true
        try {
            obj.put("title", title)
            obj.put("body", body)
            val acl = NCMBAcl()
            val currentUser = NCMBUser().getCurrentUser()
            acl.publicReadAccess = true
            acl.setUserWriteAccess(currentUser.getObjectId()!!, true)
            obj.setAcl(acl)
            Log.d("INFO",  "保存実行")
            obj.save()
            title = ""
            body = ""
            message = "日報を保存しました"
            Log.d("INFO", obj.getCreateDate().toString())
        } catch(e: NCMBException){
            Log.d("INFO",  e.message)
            message = "日報が保存できませんでした"
        }
        showDialog = true
        progress = false
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("日報の新規作成")
                },
                actions = {
                    IconButton(
                        onClick = {
                        }
                    ){
                        Icon(Icons.Filled.Save, contentDescription = "Save")
                    }
                }
            )
        },
        content = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.padding(20.dp),
                    label = { Text("日報のタイトル") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    modifier = Modifier.padding(20.dp).height(150.dp),
                    label = { Text("日報の本文") },

                )
                Button(onClick = save,
                    enabled = !progress,
                    modifier = Modifier.padding(20.dp)
                ) {
                    if (progress) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("保存する")
                    }
                }
            }
        }
    )

}