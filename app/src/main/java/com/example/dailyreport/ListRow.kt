package com.example.dailyreport

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.nifcloud.mbaas.core.NCMBObject
import org.json.JSONObject

@Composable
fun ListRow(obj: NCMBObject, navController: NavController) {
    val moveDetail = {
        val json = JSONObject()
        obj.keys.forEach {key ->
            json.put(key, obj.get(key)!!)
        }
        navController.navigate("detail/obj=${json.toString()}")
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color.White, RoundedCornerShape(5.dp))
            .clickable {
                moveDetail()
            }
    ) {

        Text(obj.getString("title")!!, fontSize = 25.sp)
    }

}