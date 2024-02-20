package com.flie.best.tato.secondrecording

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.flie.best.tato.secondrecording.databinding.ActivityMainBinding
import android.Manifest
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickFun()
    }

    private fun clickFun() {
        binding.atvRecord.setOnClickListener {
            checkRecordPermission {
                Intent(this, TranscribeActivity::class.java).also {
                    startActivity(it)
                }
            }
         }
        binding.atvList.setOnClickListener {
            Intent(this, TabulationActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.llSetting.setOnClickListener {
            Intent(this, SettingActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    val RECORD_AUDIO = 7910

    private fun checkRecordPermission(
        isRequest: Boolean = false,
        jump: () -> Unit,
    ) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            jump()
        } else {
            onPermission(isRequest, this)
        }
    }

    private fun onPermission(isRequest: Boolean, activity: MainActivity) {
        if (isRequest) {
            denyPermissionPopup(activity)
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO
            )
        }
    }

    private fun denyPermissionPopup(activity: MainActivity) {
        val dialog = AlertDialog.Builder(activity)
        dialog.setTitle("Tip")
            .setMessage("Recording permission denied, unable to record")
            .setPositiveButton("Setting") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
                dialog.create().dismiss()
            }
            .create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == RECORD_AUDIO){
            checkRecordPermission(true) {
                Intent(this, TranscribeActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }
}