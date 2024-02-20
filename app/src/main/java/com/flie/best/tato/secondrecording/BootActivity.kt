package com.flie.best.tato.secondrecording

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.lifecycle.lifecycleScope
import com.flie.best.tato.secondrecording.databinding.ActivityBootBinding
import com.flie.best.tato.secondrecording.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startCountdown()
        getBlackList(this)
    }
    private fun startCountdown() {
        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 2000
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            binding.pbBoot.progress = progress
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                startActivity(Intent(this@BootActivity, MainActivity::class.java))
                finish()
            }
        })
        animator.start()
    }
    private fun getBlackList(context: Context) {
        if (SjUtils.record_black_data.isNotEmpty()) {
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            SjUtils.getMapData(SjUtils.getRecordUrl(),SjUtils.cloakMapData(context), onNext = {
                Log.e("TAG", "The blacklist request is successful：$it")
                SjUtils.record_black_data = it
            }, onError = {
                retry(SjUtils.getRecordUrl(),context)
            })
        }
    }

    private fun retry(it: String, context: Context) {
        lifecycleScope.launch(Dispatchers.IO) {
            SjUtils.getMapData(it, SjUtils.cloakMapData(context), onNext = {
                Log.e("TAG", "The blacklist request is successful：$it")
                SjUtils.record_black_data = it
            }, onError = {
                Log.e("TAG", "The blacklist request failed：$it")
            })
        }
    }
}