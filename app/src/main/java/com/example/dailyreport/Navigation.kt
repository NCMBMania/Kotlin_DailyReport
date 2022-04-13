package com.example.dailyreport

import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.nifcloud.mbaas.core.NCMBAcl
import com.nifcloud.mbaas.core.NCMBBase
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBUser
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.compose.NavHost as NavHost

@Composable
fun Navigation() {
    var startDestination by remember { mutableStateOf("list") }
    val changeLoginStatus = {
        val currentUser: NCMBUser = NCMBUser().getCurrentUser()
        startDestination = if (currentUser.getObjectId() == null) "login" else "list"
        Unit
    }
    changeLoginStatus()

    fun getDate(str: String): Date {
        val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val format = SimpleDateFormat(pattern)
        format.timeZone = SimpleTimeZone(0, "UTC")
        return format.parse(str)
    }

    fun getAcl(obj: JSONObject): NCMBAcl {
        val acl = NCMBAcl()
        Log.d("INFO", obj.toString())
        obj.keys().forEach { key ->
            val map = obj.get(key) as JSONObject
            when {
                key == "*" -> {
                    if (!map.isNull("read") && map.get("read") as Boolean) {
                        acl.publicReadAccess = true
                    }
                    if (!map.isNull("write") && map.get("write") as Boolean) {
                        acl.publicWriteAccess = true
                    }
                }
                key.indexOf("role:") > -1 -> {
                    val match = "role:(.*)$".toRegex().find(key)
                    val roleName = match?.groups?.get(1)?.value
                    if (roleName != null) {
                        if (!map.isNull("read") && map.get("read") as Boolean) {
                            acl.setRoleReadAccess(roleName, true)
                        }
                        if (!map.isNull("write") && map.get("write") as Boolean) {
                            acl.setRoleWriteAccess(roleName, true)
                        }
                    }
                }
                else -> {
                    if (!map.isNull("read") && map.get("read") as Boolean) {
                        acl.setUserReadAccess(key, true)
                    }
                    if (!map.isNull("write") && map.get("write") as Boolean) {
                        acl.setUserWriteAccess(key, true)
                    }
                }
            }
        }
        return acl
    }

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = "login") {
            LoginScreen(navController, changeLoginStatus)
        }
        composable(route = "form") {
            val obj = NCMBObject("DailyReport")
            FormScreen(navController, obj)
        }
        composable(route = "list") {
            ListScreen(navController = navController)
        }
        composable(
            route = "detail/obj={obj}",
            arguments = listOf(navArgument("obj") { type = NavType.StringType})
        ) { backStackEntry ->
            val json = JSONObject(backStackEntry.arguments!!.getString("obj"))
            val obj = NCMBObject("DailyReport")
            json.keys().forEach { key ->
                obj.put(key, json.get(key))
            }
            DetailScreen(navController, obj)
        }
        composable(
            route = "edit/obj={obj}",
            arguments = listOf(navArgument("obj") { type = NavType.StringType})
        ) { backStackEntry ->
            val json = JSONObject(backStackEntry.arguments!!.getString("obj"))
            val obj = NCMBObject("DailyReport")
            json.keys().forEach { key ->
                when (key) {
                    "objectId" -> { obj.setObjectId(json.get(key) as String)}
                    "createDate" -> { obj.setCreateDate(getDate(json.get(key) as String))}
                    "updateDate" -> {} // obj.put("updateDate", getDate(json.get(key) as String))}
                    "acl" -> {obj.setAcl(getAcl(json.get(key) as JSONObject))}
                    else -> obj.put(key, json.get(key))
                }
            }
            FormScreen(navController, obj)
        }
    }
}