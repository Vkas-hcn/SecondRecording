package com.flie.best.tato.secondrecording

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.flie.best.tato.secondrecording.databinding.ActivityListRecordBinding
import com.flie.best.tato.secondrecording.databinding.ActivityMainBinding
import com.flie.best.tato.secondrecording.databinding.ActivitySettingBinding
import com.flie.best.tato.secondrecording.databinding.ActivityTranscribeBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickFun()
    }

    private fun clickFun(){
        binding.imageView2.setOnClickListener {
            finish()
        }
        binding.textView2.setOnClickListener {
            Intent(this, PolicyActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}