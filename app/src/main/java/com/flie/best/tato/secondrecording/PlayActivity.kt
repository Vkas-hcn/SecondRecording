package com.flie.best.tato.secondrecording

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import com.flie.best.tato.secondrecording.databinding.ActivityPlayRecordBinding
import java.io.File
import java.text.DecimalFormat

class PlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayRecordBinding
    private var playSpeedNum = 0.0f
    private lateinit var audioPlayer: AudioUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        clickFun()
    }

    private fun clickFun(){
        binding.imageView2.setOnClickListener {
            finish()
        }
        binding.tv05.setOnClickListener {
            playMultipleTimes(0.5f)
        }
        binding.tv1.setOnClickListener {
            playMultipleTimes(1.0f)
        }
        binding.tv2.setOnClickListener {
            playMultipleTimes(1.5f)
        }
        binding.tv3.setOnClickListener {
            playMultipleTimes(2.0f)
        }
        binding.aivPlayStop.setOnClickListener {
            resumeAudioFun()
        }
    }
    var fileName = ""
    var fileTime: Long = 0
    var fileData: File? = null
    private fun playAudio() {
        fileData?.let { it1 ->
            audioPlayer.playAudio(it1, (playSpeedNum / 10))
            handler.post(updateSeekBar)
        }
        binding.aivPlayStop.setImageResource(R.drawable.icon_stop)
    }

    private fun resumeAudioFun() {
        if (audioPlayer.isPlaying()) {
            binding.aivPlayStop.setImageResource(R.drawable.ic_play_record)
            audioPlayer.pauseAudio()
            handler.removeCallbacks(updateSeekBar)
        } else {
            binding.aivPlayStop.setImageResource(R.drawable.icon_stop)
            audioPlayer.resumeAudio()
            handler.post(updateSeekBar)
        }

    }
    private fun initData() {
        fileName = intent.getStringExtra("fileName").toString()
        fileTime = intent.getLongExtra("fileTime", 0)
        audioPlayer = AudioUtils()
        if (fileName.isNotEmpty()) {
            binding.textView.text = fileName
            fileData = SjUtils.getFilePathByName(this,fileName)
            playSpeedNum = 10.0f
            playAudio()
            initSeekBar()
        }
    }

    private fun initSeekBar() {
        binding.progressBarRecord.max = (fileTime).toInt()
        binding.progressBarRecord.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    audioPlayer.setTOProgress(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun playMultipleTimes(num: Float) {
        playSpeedNum = num
        updateSpeedUI()
        audioPlayer.changeSpeed(playSpeedNum)
    }

    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekBar: Runnable = object : Runnable {
        override fun run() {
            if (audioPlayer.isPlaying()) {
                binding.progressBarRecord.progress = audioPlayer.getCurrentPosition()
                val timeData = formatTime((audioPlayer.getCurrentPosition()) / 1000)
                binding.appCompatTextView2.text = timeData
                handler.postDelayed(this, 100)
            } else {
                binding.progressBarRecord.progress = audioPlayer.getCurrentPosition()
                binding.aivPlayStop.setImageResource(R.drawable.ic_play_record)
            }
        }
    }
    fun formatTime(timerData: Int): String {
        val mm: String = DecimalFormat("00").format(timerData % 3600 / 60)
        val ss: String = DecimalFormat("00").format(timerData % 60)
        return "$mm:$ss"
    }

    private fun updateSpeedUI() {
        when (playSpeedNum) {
            0.5f -> {
                binding.tv05.setBackgroundResource(R.drawable.bg_rate)
                binding.tv1.setBackgroundResource(R.drawable.bg_rate_2)
                binding.tv2.setBackgroundResource(R.drawable.bg_rate_2)
                binding.tv3.setBackgroundResource(R.drawable.bg_rate_2)
                binding.tv05.setTextColor(resources.getColor(R.color.rate))
                binding.tv1.setTextColor(resources.getColor(R.color.rate2))
                binding.tv2.setTextColor(resources.getColor(R.color.rate2))
                binding.tv3.setTextColor(resources.getColor(R.color.rate2))
            }
            1.0f -> {
                binding.tv05.setBackgroundResource(R.drawable.bg_rate_2)
                binding.tv1.setBackgroundResource(R.drawable.bg_rate)
                binding.tv2.setBackgroundResource(R.drawable.bg_rate_2)
                binding.tv3.setBackgroundResource(R.drawable.bg_rate_2)
                binding.tv05.setTextColor(resources.getColor(R.color.rate2))
                binding.tv1.setTextColor(resources.getColor(R.color.rate))
                binding.tv2.setTextColor(resources.getColor(R.color.rate2))
                binding.tv3.setTextColor(resources.getColor(R.color.rate2))
            }
            1.5f -> {
                binding.tv05.setBackgroundResource(R.drawable.bg_rate_2)
                binding.tv1.setBackgroundResource(R.drawable.bg_rate_2)
                binding.tv2.setBackgroundResource(R.drawable.bg_rate)
                binding.tv3.setBackgroundResource(R.drawable.bg_rate_2)
                binding.tv05.setTextColor(resources.getColor(R.color.rate2))
                binding.tv1.setTextColor(resources.getColor(R.color.rate2))
                binding.tv2.setTextColor(resources.getColor(R.color.rate))
                binding.tv3.setTextColor(resources.getColor(R.color.rate2))
            }
            2.0f -> {
                binding.tv05.setBackgroundResource(R.drawable.bg_rate_2)
                binding.tv1.setBackgroundResource(R.drawable.bg_rate_2)
                binding.tv2.setBackgroundResource(R.drawable.bg_rate_2)
                binding.tv3.setBackgroundResource(R.drawable.bg_rate)
                binding.tv05.setTextColor(resources.getColor(R.color.rate2))
                binding.tv1.setTextColor(resources.getColor(R.color.rate2))
                binding.tv2.setTextColor(resources.getColor(R.color.rate2))
                binding.tv3.setTextColor(resources.getColor(R.color.rate))
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        audioPlayer.stopAudio()
        handler.removeCallbacks(updateSeekBar)
    }
}