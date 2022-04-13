package com.example.dailyreport

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.nifcloud.mbaas.core.NCMBCallback
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import androidx.compose.material.Text
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

@Composable
fun ListScreen(navController: NavController) {
    var ary = remember { mutableStateOf<List<NCMBObject>>(emptyList()) }
    val query = NCMBQuery.forObject("DailyReport")
    query.findInBackground(NCMBCallback { e, results ->
        if (e == null) {
            ary.value = results as List<NCMBObject>
        }
    })
    Scaffold(
        topBar = {
                TopAppBar(
                    title = {
                        Text("日報一覧")
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.navigate("form")
                            }
                        ){
                            Icon(Icons.Filled.Add, contentDescription = "日報の作成")
                        }
                    }
                )
        },
        content = {
            Column {
                LazyColumn(
                ) {
                    items(ary.value) { obj ->
                        ListRow(obj, navController = navController)
                    }
                }
            }
        }
    )
}