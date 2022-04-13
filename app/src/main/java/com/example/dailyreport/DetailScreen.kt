package com.example.dailyreport

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nifcloud.mbaas.core.NCMBObject
import org.json.JSONObject


@Composable
fun DetailScreen(navController: NavController, obj: NCMBObject) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("日報")
                },
                actions = {
                    IconButton(
                        onClick = {
                            val json = JSONObject()
                            obj.keys.forEach {key ->
                                json.put(key, obj.get(key)!!)
                            }
                            navController.navigate("edit/obj=${json.toString()}")
                        }
                    ){
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                }
            )
        },
        content = {
            Column {
                Text(text = obj.getString("title")!!,
                    fontSize = 35.sp,
                    modifier = Modifier.padding(5.dp)
                )
                Text(text = obj.getString("body")!!,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    )
}