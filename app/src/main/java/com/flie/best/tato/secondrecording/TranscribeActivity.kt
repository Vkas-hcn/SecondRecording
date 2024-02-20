package com.flie.best.tato.secondrecording

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import com.flie.best.tato.secondrecording.databinding.ActivityMainBinding
import com.flie.best.tato.secondrecording.databinding.ActivityTranscribeBinding

class TranscribeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTranscribeBinding
    var recordingName: String = ""
    lateinit var audioUtils: AudioUtils
    lateinit var audioFilePath: String
    private var pauseOffset: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTranscribeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        audioUtils = AudioUtils()
        clickFun()
    }

    private fun clickFun() {
        binding.aivPlayRecord.setOnClickListener {
            onClickPlayOrStop()
        }
        binding.aivPlayEnd.setOnClickListener {
            onClickEnd()
        }
        binding.imageView2.setOnClickListener {
            finish()
        }
    }

    private fun startRecording() {
        SjUtils.baseFilePath = getExternalFilesDir(null)?.absolutePath.toString()
        recordingName = "${getNowDateTime()}.m4a"
        audioFilePath = SjUtils.baseFilePath + "/$recordingName"
        audioUtils.startRecording(audioFilePath)
        binding.aivPlayRecord.setImageResource(R.drawable.icon_stop)
    }

    private fun onClickPlayOrStop() {
        if (audioUtils.isInitRecording()) {
            if (audioUtils.isRecording()) {
                audioUtils.pauseRecording()
                binding.aivPlayRecord.setImageResource(R.drawable.ic_play_record)
                pauseChronometer()
            } else {
                audioUtils.resumeRecording()
                binding.aivPlayRecord.setImageResource(R.drawable.icon_stop)
                resumeChronometer()
            }
        } else {
            startRecording()
            startChronometer()
        }
    }

    private fun onClickEnd() {
        audioUtils.stopRecording()
        binding.aivPlayRecord.setImageResource(R.drawable.ic_play_record)
        pauseChronometer()
        binding.appCompatTextView2.base = SystemClock.elapsedRealtime()
        Toast.makeText(this, "Recording completed. Go to the list to view.", Toast.LENGTH_SHORT)
            .show()
    }

    private fun startChronometer() {
        pauseOffset = 0
        binding.appCompatTextView2.base = SystemClock.elapsedRealtime() - pauseOffset
        binding.appCompatTextView2.start()
    }

    private fun pauseChronometer() {
        binding.appCompatTextView2.stop()
        pauseOffset = SystemClock.elapsedRealtime() - binding.appCompatTextView2.base
    }

    private fun resumeChronometer() {
        binding.appCompatTextView2.base = SystemClock.elapsedRealtime() - pauseOffset
        binding.appCompatTextView2.start()
    }
    //当前日期
    private fun getNowDateTime(): String {
        val date = java.util.Date()
        val sdf = java.text.SimpleDateFormat("MM-dd_HH-mm-ss")
        return sdf.format(date)
    }
    override fun onDestroy() {
        super.onDestroy()
        if (audioUtils.isRecording()) {
            SjUtils.deleteFileByName(this, recordingName)
        }
        audioUtils.stopRecording()
    }
}