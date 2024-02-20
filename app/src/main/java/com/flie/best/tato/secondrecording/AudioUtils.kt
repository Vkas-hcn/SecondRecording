package com.flie.best.tato.secondrecording

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.PlaybackParams
import java.io.File
import java.io.IOException

class AudioUtils {

    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: String? = null
    private var isRecording = false
    fun startRecording(filePath: String) {
        outputFile = filePath
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile)
            prepare()
            start()
        }
        isRecording = true
    }

    fun pauseRecording() {
        mediaRecorder?.pause()
        isRecording = false
    }

    fun resumeRecording() {
        mediaRecorder?.resume()
        isRecording = true
    }

    fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        isRecording = false
    }

    fun isRecording(): Boolean {
        return isRecording
    }
    fun isInitRecording(): Boolean {
        return mediaRecorder != null
    }

    private var mediaPlayer: MediaPlayer? = null

    fun playAudio(file: File, speed: Float) {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(file.absolutePath)
                prepare()
                playbackParams = PlaybackParams().setSpeed(speed)
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false
    fun pauseAudio() = mediaPlayer?.pause()
    fun resumeAudio() = mediaPlayer?.start()
    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0
    fun setTOProgress(progress: Int) = mediaPlayer?.seekTo(progress)


    fun changeSpeed(speed: Float) {
        mediaPlayer?.apply {
            playbackParams = playbackParams?.setSpeed(speed) ?: PlaybackParams().setSpeed(speed)
            if (!isPlaying) {
                pauseAudio()
            }
        }
    }
}