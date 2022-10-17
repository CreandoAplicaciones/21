package com.vegano.en21.dias.ui.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.ads.*
import com.vegano.en21.dias.R
import com.vegano.en21.dias.databinding.ActivityMainBinding
import com.vegano.en21.dias.ui.base.BaseActivity



class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    var backPressedTime: Long = 0

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        binding.adViewSup.loadAd(adRequest)
        binding.adViewInf.loadAd(adRequest)
    }
}


