package com.flie.best.tato.secondrecording

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.flie.best.tato.secondrecording.App.Companion.mmkvRecord
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Date

object SjUtils {
    fun getPolicyUrl(): String {
        return "https://www.baidu.com/"
    }
    fun getRecordUrl(): String {
        return "https://silvery.voicetracksense.com/vermont/position/flange"
    }
    var baseFilePath = ""
    var uuid_record: String = ""
        set(value) {
            mmkvRecord.encode("uuid_record", value)
            field = value
        }
        get() = mmkvRecord.decodeString("uuid_record", "") ?: ""

    var record_black_data: String = ""
        set(value) {
            mmkvRecord.encode("record_black_data", value)
            field = value
        }
        get() = mmkvRecord.decodeString("record_black_data", "") ?: ""
    fun cloakMapData(context: Context): Map<String, Any> {
        return mapOf<String, Any>(
            //distinct_id
            "bran" to (uuid_record),
            //client_ts
            "buxton" to (System.currentTimeMillis()),
            //device_model
            "pip" to Build.MODEL,
            //bundle_id
            "markham" to ("com.voice.track.sense.memo.pro"),
            //os_version
            "mentor" to Build.VERSION.RELEASE,
            //gaid
            "tighten" to "",
            //android_id
            "pervert" to context.getAppId(),
            //os
            "barbital" to "tripod",
            //app_version
            "heretic" to context.getAppVersion(),
        )
    }

    private fun Context.getAppVersion(): String {
        try {
            val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)

            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "Version information not available"
    }

    @SuppressLint("HardwareIds")
    private fun Context.getAppId(): String {
        return Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

     fun getMapData(url: String, map: Map<String, Any>, onNext: (response: String) -> Unit, onError: (error: String) -> Unit) {
        val queryParameters = StringBuilder()
        for ((key, value) in map) {
            if (queryParameters.isNotEmpty()) {
                queryParameters.append("&")
            }
            queryParameters.append(URLEncoder.encode(key, "UTF-8"))
            queryParameters.append("=")
            queryParameters.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }

        val urlString = if (url.contains("?")) {
            "$url&$queryParameters"
        } else {
            "$url?$queryParameters"
        }

        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 15000

        try {
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                inputStream.close()
                onNext(response.toString())
            } else {
                onError("HTTP error: $responseCode")
            }
        } catch (e: Exception) {
            onError("Network error: ${e.message}")
        } finally {
            connection.disconnect()
        }
    }



    fun getRecordingsFromStorage(context: Context): MutableList<AutoBean> {
        if (baseFilePath.isEmpty()) {
            baseFilePath = context.getExternalFilesDir(null)!!.absolutePath
        }
        val directory = File(baseFilePath)
        val recordings = mutableListOf<AutoBean>()
        directory.listFiles()?.forEach { file ->
            if (file.isFile) {
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(file.path)
                mediaPlayer.prepare()
                val duration = mediaPlayer.duration.toLong()
                mediaPlayer.release()
                val name = file.name
                val date = Date(file.lastModified())
                recordings.add(AutoBean(name, duration, date))
            }
        }
        return recordings.sortedByDescending { it.date }.toMutableList()
    }

     fun getFilePathByName(context: Context, name: String): File? {
        if (baseFilePath.isEmpty()) {
            baseFilePath = context.getExternalFilesDir(null)!!.absolutePath
        }
        val directory = File(baseFilePath)
        directory.listFiles()?.forEach { file ->
            if (file.isFile) {
                if (file.name == name) {
                    return file
                }
            }
        }
        return null
    }
    fun deleteFileByName(context: Context,name: String) {
        val file = getFilePathByName(context,name)
        file?.delete()
    }
    fun renameFileByName(context: Context,oldName: String, newName: String) {
        val file = getFilePathByName(context,oldName)
        file?.renameTo(File("$baseFilePath/${newName}.m4a"))
    }
}